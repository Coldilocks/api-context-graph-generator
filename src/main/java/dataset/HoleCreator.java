package dataset;

import com.github.javaparser.utils.Pair;
import entity.Graph;
import entity.GraphNode;
import org.apache.commons.collections4.CollectionUtils;
import util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coldilock
 */
public class HoleCreator {

    private static final int MAX_HOLE_SIZE = 5;

    private GraphNode rootNode;

    // 完整的节点集合
    private List<GraphNode> graphNodeList;

    // 完整的节点图
    private Map<String, List<Pair<String, String>>> edgeMap;

    // 程序依赖图所属文件名
    private String fileName;

    // 程序以来图所属方法名
    private String methodName;

    public HoleCreator(GraphNode rootNode){
        this.rootNode = rootNode;
    }

    public HoleCreator(GraphNode rootNode, List<GraphNode> graphNodeList, Map<String, List<Pair<String, String>>> edgeMap){
        this.rootNode = rootNode;
        this.graphNodeList = graphNodeList;
        this.edgeMap = edgeMap;
    }

    public HoleCreator(GraphNode rootNode, List<GraphNode> graphNodeList,
                       Map<String, List<Pair<String, String>>> edgeMap,
                       String fileName, String methodName){
        this.rootNode = rootNode;
        this.graphNodeList = graphNodeList;
        this.edgeMap = edgeMap;
        this.fileName = fileName;
        this.methodName = methodName;
    }

    public void createHoleWithHoleRange() {
        for(int i = 1; i <= MAX_HOLE_SIZE; i++){
            markHoleNodeWithHoleSize(i);
        }
    }

    /**
     * 挖窟窿时不需要在树上进行操作，可以对GraphNode的list直接进行操作
     * 并且无需考虑list中node的顺序问题，因为可以根据node.getChildNodes()知道它们之间的关系
     * @param holeSize
     */
    public void markHoleNodeWithHoleSize(int holeSize){
        for(GraphNode beginNode : this.graphNodeList){

            // 被挖去的节点
            List<GraphNode> holeNodes = new ArrayList<>();
            // 因为是被挖去节点的孩子，而一同被删除的节点
            List<GraphNode> childNodesOfHoleNodes = new ArrayList<>();
            // 当前节点
            GraphNode currentNode = beginNode;
            // 结束节点
            GraphNode endNode = beginNode;
            // 当前窟窿大小
            int currentHoleSize = 0;

            // 开始挖窟窿
            while(currentNode != null && currentHoleSize < holeSize){
                holeNodes.add(currentNode);
                currentHoleSize++;

                if(isControlNode(currentNode)){
                    // 如果是currentNode是控制节点，获取它的内部子节点，加入到要删除的节点集合中
                    List<GraphNode> realChildNodes = getChildNodesInsideControlNode(currentNode);
                    childNodesOfHoleNodes.addAll(realChildNodes);
                }
                endNode = currentNode;
                currentNode = currentNode.getNextNode();
            }


            // 构造数据集
            createHoleWithHoleSize(holeNodes, childNodesOfHoleNodes, beginNode, endNode, holeSize);

        }

    }

    /**
     * 根据窟窿中的节点
     *  删除节点集合中，所有出现在窟窿里的节点
     *  创建hole节点，添加到节点集合中
     *  创建新的边map，删除c、d、cd边集合中，所有和窟窿中节点有关联的边，并创建特殊边s集合
     * @param holeNodes
     * @param childNodesOfHoleNodes
     * @param beginNode
     * @param endNode
     * @param holeSize
     */
    public void createHoleWithHoleSize(List<GraphNode> holeNodes, List<GraphNode> childNodesOfHoleNodes, GraphNode beginNode, GraphNode endNode, int holeSize){
        /*
         * 1.创建hole节点
         * 删除节点集合中，所有出现在窟窿里的节点
         * 将hole节点插入到新的节点集合中
         */
        // 创建hole节点
        GraphNode holeNode = new GraphNode("Hole", StringUtil.getUuid());
        holeNode.setParentNode(beginNode.getParentNode());
        holeNode.setNextNode(endNode.getNextNode());
        holeNode.setChildNodes(new ArrayList<GraphNode>(){
            {
                this.add(endNode.getNextNode());
            }
        });

        // 删除节点集合中，所有出现在窟窿里的节点
        List<GraphNode> graphNodeListWithHole = new ArrayList<>(graphNodeList);
        int holeBeginIndex = graphNodeListWithHole.indexOf(beginNode);

        graphNodeListWithHole.removeAll(holeNodes);
        graphNodeListWithHole.removeAll(childNodesOfHoleNodes);

        // 将hole节点插入到节点集合中
        graphNodeListWithHole.add(holeBeginIndex, holeNode);

        // 获取holeNodes和deletedChildNodesId的id
        List<String> holeNodesId = holeNodes.stream()
                .map(GraphNode::getId).collect(Collectors.toList());

        List<String> deletedChildNodesId = childNodesOfHoleNodes.stream()
                .map(GraphNode::getId).collect(Collectors.toList());

        /*
         * 2. 创建新的边map，并创建特殊边s集合
         */
        Map<String, List<Pair<String, String>>> edgeMapWithHole = new HashMap<>();

        List<Pair<String, String>> specialFlowPairs = new ArrayList<>();

        // 窟窿中第一个节点的入边
        Pair<String,String> beginEdge = null;
        if(beginNode.getParentNode() != null){
            // 获取原始完整的图中，连接窟窿中第一个节点的入边
            beginEdge = new Pair<>(beginNode.getParentNode().getId(), beginNode.getId());
            // 创建新的连接窟窿的入边，并加入到s集合中
            Pair<String, String> holeBeginEdge = new Pair<>(beginNode.getParentNode().getId(), holeNode.getId());
            specialFlowPairs.add(holeBeginEdge);
        }

        // 窟窿中最后一个节点的出边
        Pair<String,String> endEdge = null;
        if(endNode.getNextNode() != null){
            // 获取原始完整的图中，连接窟窿中最后一个节点的出边
            endEdge = new Pair<>(endNode.getId(), endNode.getNextNode().getId());
            // 创建新的连接窟窿的出边，并加入到s集合中
            Pair<String, String> holeEndEdge = new Pair<>(holeNode.getId(), endNode.getNextNode().getId());
            specialFlowPairs.add(holeEndEdge);
        }

        edgeMapWithHole.put("s", specialFlowPairs);

        /*
         * 3.删除控制流边集合中，所有和窟窿中节点有关联的边
         * 对于c边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) && holeNodesId.contains(b)：删除这个Pair
         * 或者 deletedChildNodesId.contains(a) || deletedChildNodesId.contains(b)：删除这个Pair
         * 或者 (beginNodeParentId, beginNode)、(endNode, NextNodeOfEndNode)在c边集合中，删除这个Pair
         */
        Pair<String, String> finalBeginEdge = beginEdge;
        Pair<String, String> finalEndEdge = endEdge;

        // 过滤控制流边中，和窟窿中节点相关的边
        List<Pair<String, String>> controlFlowPairs = this.edgeMap.get("c").stream()
                .filter(edge -> (!holeNodesId.contains(edge.a) || !holeNodesId.contains(edge.b))
                        && !deletedChildNodesId.contains(edge.a)
                        && !deletedChildNodesId.contains(edge.b)
                        && !edge.equals(finalBeginEdge)
                        && !edge.equals(finalEndEdge))
                .collect(Collectors.toList());

        edgeMapWithHole.put("c", controlFlowPairs);

        /*
         * 4.删除控制数据流边集合中，所有和窟窿中节点有关联的边
         * 对于cd边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) && holeNodesId.contains(b)：删除这个Pair
         * 或者 deletedChildNodesId.contains(a) || deletedChildNodesId.contains(b)：删除这个Pair
         * 或者 (beginNodeParentId, beginNode)、(endNode, NextNodeOfEndNode)在c边集合中，删除这个Pair
         */
        List<Pair<String, String>> controlFlowAndDataFlowPairs = this.edgeMap.get("cd").stream()
                .filter(edge -> (!holeNodesId.contains(edge.a) || !holeNodesId.contains(edge.b))
                        && !deletedChildNodesId.contains(edge.a)
                        && !deletedChildNodesId.contains(edge.b)
                        && !edge.equals(finalBeginEdge)
                        && !edge.equals(finalEndEdge))
                .collect(Collectors.toList());

        edgeMapWithHole.put("cd", controlFlowAndDataFlowPairs);

        /*
         * 5.删除数据流边集合中，所有和窟窿中节点有关联的边
         * 对于d边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) || holeNodesId.contains(b) || deletedChildNodesId.contains(a) || deletedChildNodesId.contains(b)
         * 那么删除这个Pair
         */
        List<Pair<String, String>> dataFlowPairs = this.edgeMap.get("d").stream()
                .filter(pair -> !holeNodesId.contains(pair.a)
                        && !holeNodesId.contains(pair.b)
                        && !deletedChildNodesId.contains(pair.a)
                        && !deletedChildNodesId.contains(pair.b))
                .collect(Collectors.toList());

        edgeMapWithHole.put("d", dataFlowPairs);

        /*
         * 生成单个数据
         */
        // 记录Graph Vocab 和 Graph Representation
        this.getGraphVocabAndRepresentation(graphNodeListWithHole, edgeMapWithHole);
        // 记录trace
        DataCollector.traceList.add(this.methodName + " (" + this.fileName + ")");
        // 记录hole size
        DataCollector.holeSizeList.add(String.valueOf(holeSize));
        // 记录beginNode的name，得到prediction
        DataCollector.singlePredictionList.add(beginNode.getNodeName());
        // 记录block prediction
        DataCollector.blockPredictionList.add(this.getBlockPrediction(beginNode, holeNodes, childNodesOfHoleNodes));
    }

    /**
     * 生成Graph Vocab 和 Graph Representation
     * @param graphNodeListWithHole
     * @param edgeMapWithHole
     */
    public void getGraphVocabAndRepresentation(List<GraphNode> graphNodeListWithHole, Map<String, List<Pair<String, String>>> edgeMapWithHole){
        /*
         * 创建一个旧id到新id的映射
         * 并生成Graph Vocab
         */
        Map<String, String> idChangeMap = new HashMap<>();
        List<String> graphVocab = new ArrayList<>();

        for(int i = 0; i < graphNodeListWithHole.size(); i++){
            idChangeMap.put(graphNodeListWithHole.get(i).getId(), String.valueOf(i + 1));
            // sortedGraphNodes.get(i).setId(String.valueOf(i + 1));
            // id + ":" + '\'' + nodeName + '\'';
            graphVocab.add((i + 1) + ":" + '\'' + graphNodeListWithHole.get(i).getNodeName() + '\'');
        }

        DataCollector.graphVocabList.add(graphVocab);

        /*
         * 生成Graph Representation
         */
        List<String> formatEdges = new ArrayList<>();

        formatEdges.addAll(this.changeIdAndGetGraphRepresentation(edgeMapWithHole, idChangeMap, "c"));
        formatEdges.addAll(this.changeIdAndGetGraphRepresentation(edgeMapWithHole, idChangeMap, "d"));
        formatEdges.addAll(this.changeIdAndGetGraphRepresentation(edgeMapWithHole, idChangeMap, "cd"));
        formatEdges.addAll(this.changeIdAndGetGraphRepresentation(edgeMapWithHole, idChangeMap, "s"));

        DataCollector.graphReprensentList.add(formatEdges);

    }

    /**
     * 获取block prediction
     * @param beginNode
     * @param holeNodes
     * @param deletedChildNodes
     * @return
     */
    public String getBlockPrediction(GraphNode beginNode, List<GraphNode> holeNodes, List<GraphNode> deletedChildNodes){

        // 对beginNode进行深度优先遍历，只保留在holeNodes和deletedChildNodes中的节点, 这些节点作为blockPrediciton
        // 这样可以保证blockPrediciton里的节点都是按深度优先顺序的
        holeNodes.addAll(deletedChildNodes);

        Graph graph = new Graph();
        List<GraphNode> blockPredictionNodes = graph.getGraphNodesDFS(beginNode);
        blockPredictionNodes.retainAll(holeNodes);

        StringBuilder blockPredicitonStr = new StringBuilder();
        for(GraphNode holeNode : blockPredictionNodes){
            blockPredicitonStr.append(holeNode.getNodeName()).append(" ");
        }

        return blockPredicitonStr.toString();
    }

    /**
     * 更改节点的id，并生成某种边对应的Graph Representation
     * @param edgeMapWithHole
     * @param idChangeMap
     * @param edgeType
     * @return
     */
    public List<String> changeIdAndGetGraphRepresentation(Map<String, List<Pair<String, String>>> edgeMapWithHole, Map<String, String> idChangeMap, String edgeType){

        String edgeTypeNum = "";
        switch (edgeType) {
            case "c":
                edgeTypeNum = "1";
                break;
            case "d":
                edgeTypeNum = "2";
                break;
            case "cd":
                edgeTypeNum = "3";
                break;
            case "s":
                edgeTypeNum = "4";
                break;
        }

        // todo: 把edgeMapWithHole、idChangeMap设置为全局变量
        List<String> formatEdges = new ArrayList<>();
        for(Pair<String, String> edge : edgeMapWithHole.get(edgeType)){

            String formatEdge = "[" +
                    idChangeMap.get(edge.a) +
                    "," +
                    edgeTypeNum +
                    "," +
                    idChangeMap.get(edge.b) +
                    "]";
            formatEdges.add(formatEdge);
        }
        return formatEdges;
    }

    /**
     * 查找节点集合中的根节点
     * @param graphNodes
     * @return
     */
    public GraphNode findRootNode(List<GraphNode> graphNodes){
        return graphNodes.stream()
                .filter(graphNode -> graphNode.getParentNode() == null)
                .collect(Collectors.toList()).get(0);
    }

    /**
     * 判断是否为控制流节点
     * @param graphNode
     * @return
     */
    public boolean isControlNode(GraphNode graphNode){
        String[] controlNodeNames = new String[]{"If", "For", "ForEach", "While", "doWhile", "Try", "Switch"};
        List<String> controlNodeNameList = Arrays.asList(controlNodeNames);
        return controlNodeNameList.contains(graphNode.getNodeName());
    }

    /**
     * 获取控制节点的内部子节点
     * @param node 控制节点，如if、doWhile、while...
     * @return
     */
    public List<GraphNode> getChildNodesInsideControlNode(GraphNode node){
        // 控制节点的内部节点，例如if节点的内部节点有conditon节点、then节点和else节点，以及这些节点的子节点...
        GraphNode nextNode = node.getNextNode();
        List<GraphNode> directChildNodes = new ArrayList<>(CollectionUtils.emptyIfNull(node.getChildNodes()));

        if(!directChildNodes.isEmpty() && nextNode != null){
            directChildNodes.remove(nextNode);
        }

        // 对copiedChildNodes中的节点进行深度优先遍历，拿到整个子树中的所有节点
        Graph graph = new Graph();
        List<GraphNode> insideNodes = new ArrayList<>();
        for(GraphNode graphNode : directChildNodes){
            insideNodes.addAll(graph.getGraphNodesDFS(graphNode));
        }

        return insideNodes;

    }

}
