package dataset;

import com.github.javaparser.utils.Pair;
import entity.Graph;
import entity.GraphNode;
import org.apache.commons.collections4.CollectionUtils;
import util.DataConfig;
import util.FileUtil;
import util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>Hole Creator</h1>
 * <ol>
 *     <li>根据窟窿大小，从节点集合中挑选开始节点</li>
 *     <li>根据窟窿大小，以及窟窿的开始节点，返回窟窿的结束节点</li>
 *     <li>根据窟窿的第一个和最后一个节点，对节点集合和边集合进行修改，得到带有hole节点的节点集合和边map</li>
 *     <li>将生成的一行数据存储到DataCollector中</li>
 * </ol>
 * @author coldilock
 */
public class HoleCreator {

    private static final int MAX_HOLE_SIZE = 5;

    /** 完整的节点集合 */
    private final List<GraphNode> graphNodeList;

    /** 完整的节点图 */
    private final Map<String, List<Pair<String, String>>> edgeMap;

    /** 程序依赖图所属文件名 */
    private String fileName;

    /** 程序以来图所属方法名 */
    private String methodName;

    /** 挖掉的窟窿中的节点 */
    private List<GraphNode> holeNodes;

    /** 挖掉的窟窿中的节点的孩子节点 */
    private List<GraphNode> childNodesOfHoleNodes;

    /** 最终的节点集合：删除窟窿中节点并加入hole节点 */
    private List<GraphNode> graphNodeListWithHole;

    /** 最终的边map：删除窟窿中节点对应的边并加入包含hole节点的边 **/
    private Map<String, List<Pair<String, String>>> edgeMapWithHoleNode;

    /** 记录节点id的映射，用于生成文件 **/
    private Map<String, String> idChangeMap;

    public HoleCreator(List<GraphNode> graphNodeList, Map<String, List<Pair<String, String>>> edgeMap){
        this.graphNodeList = graphNodeList;
        this.edgeMap = edgeMap;
    }

    public HoleCreator(List<GraphNode> graphNodeList,
                       Map<String, List<Pair<String, String>>> edgeMap,
                       String fileName, String methodName) {
        this.graphNodeList = graphNodeList;
        this.edgeMap = edgeMap;
        this.fileName = fileName;
        this.methodName = methodName;
    }

    /**
     * 1. 挖窟窿
     * 不需要在树上进行操作，可以对GraphNode的list直接进行操作
     * 并且无需考虑list中node的顺序问题，因为可以根据node.getChildNodes()知道它们之间的关系
     */
    public void createHole() {
        for(int holeSize = 1; holeSize <= MAX_HOLE_SIZE; holeSize++){
            for(GraphNode holeBeginNode : this.graphNodeList){
                // 2.根据窟窿大小和窟窿中第一个节点，获取窟窿中最后一个节点
                GraphNode holeEndNode = this.getHoleEndNode(holeSize, holeBeginNode);
                // 3.根据窟窿的第一个和最后一个节点，对节点集合和边集合进行修改，得到带有hole节点的节点集合和边map
                this.addHoleNode(holeBeginNode, holeEndNode);
                // 4.将生成的一行数据存储到DataCollector中
                this.saveData(holeSize, holeBeginNode);
            }
        }
    }

    /**
     * 2. 根据窟窿大小，以及窟窿的开始节点，返回窟窿的结束节点
     * @param holeSize 窟窿大小
     * @param holeBeginNode 窟窿开始节点
     * @return
     */
    public GraphNode getHoleEndNode(int holeSize, GraphNode holeBeginNode){
        // 被挖去的节点
        this.holeNodes = new ArrayList<>();
        // 因为是被挖去节点的孩子，而一同被删除的节点
        this.childNodesOfHoleNodes = new ArrayList<>();
        // 当前节点
        GraphNode currentNode = holeBeginNode;
        // 结束节点
        GraphNode holeEndNode = holeBeginNode;
        // 当前窟窿大小
        int currentHoleSize = 0;

        // 开始挖窟窿
        while(currentNode != null && currentHoleSize < holeSize){
            this.holeNodes.add(currentNode);
            currentHoleSize++;

            if(isControlNode(currentNode)){
                // 如果是currentNode是控制节点，获取它的内部子节点，加入到要删除的节点集合中
                List<GraphNode> realChildNodes = this.getChildNodesInsideControlNode(currentNode);
                this.childNodesOfHoleNodes.addAll(realChildNodes);
            }
            holeEndNode = currentNode;
            currentNode = currentNode.getNextNode();
        }

        return holeEndNode;
    }

    /**
     * 3. 生成带hole节点的节点集合和边map
     * - 3.1 创建hole节点，添加到节点集合中；删除节点集合中，所有出现在窟窿里的节点
     * - 3.2 创建新的边map，删除c、d、cd边集合中，所有和窟窿中节点有关联的边，并创建特殊边s集合
     * @param holeBeginNode
     * @param holeEndNode
     */
    public void addHoleNode(GraphNode holeBeginNode, GraphNode holeEndNode){
        /*
         * 3.1 创建hole节点
         * 删除节点集合中，所有出现在窟窿里的节点
         * 将hole节点插入到新的节点集合中
         */
        // 创建hole节点
        GraphNode holeNode = new GraphNode("Hole", StringUtil.getUuid());
        holeNode.setParentNode(holeBeginNode.getParentNode());
        holeNode.setNextNode(holeEndNode.getNextNode());
        holeNode.setChildNodes(new ArrayList<GraphNode>(){
            {
                this.add(holeEndNode.getNextNode());
            }
        });

        // 删除节点集合中，所有出现在窟窿里的节点
        this.graphNodeListWithHole = new ArrayList<>(graphNodeList);
        int holeBeginIndex = this.graphNodeListWithHole.indexOf(holeBeginNode);

        this.graphNodeListWithHole.removeAll(this.holeNodes);
        this.graphNodeListWithHole.removeAll(this.childNodesOfHoleNodes);

        // 将hole节点插入到节点集合中
        this.graphNodeListWithHole.add(holeBeginIndex, holeNode);

        // 获取holeNodes和childNodesOfHoleNodes的id
        List<String> holeNodesId = this.holeNodes.stream()
                .map(GraphNode::getId).collect(Collectors.toList());

        List<String> childNodesIdOfHoleNodes = this.childNodesOfHoleNodes.stream()
                .map(GraphNode::getId).collect(Collectors.toList());

        /*
         * 3.2.1 创建新的边map，并创建特殊边s集合
         */
        this.edgeMapWithHoleNode = new HashMap<>();

        List<Pair<String, String>> specialFlowPairs = new ArrayList<>();

        // 窟窿中第一个节点的入边
        Pair<String,String> beginEdge = null;
        if(holeBeginNode.getParentNode() != null){
            // 获取原始完整的图中，连接窟窿中第一个节点的入边
            beginEdge = new Pair<>(holeBeginNode.getParentNode().getId(), holeBeginNode.getId());
            // 创建新的连接窟窿的入边，并加入到s集合中
            Pair<String, String> holeBeginEdge = new Pair<>(holeBeginNode.getParentNode().getId(), holeNode.getId());
            specialFlowPairs.add(holeBeginEdge);
        }

        // 窟窿中最后一个节点的出边
        Pair<String,String> endEdge = null;
        if(holeEndNode.getNextNode() != null){
            // 获取原始完整的图中，连接窟窿中最后一个节点的出边
            endEdge = new Pair<>(holeEndNode.getId(), holeEndNode.getNextNode().getId());
            // 创建新的连接窟窿的出边，并加入到s集合中
            Pair<String, String> holeEndEdge = new Pair<>(holeNode.getId(), holeEndNode.getNextNode().getId());
            specialFlowPairs.add(holeEndEdge);
        }
        this.edgeMapWithHoleNode.put("s", specialFlowPairs);

        /*
         * 3.2.2 删除控制流 c边 集合中，所有和窟窿中节点有关联的边
         * 对于c边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) && holeNodesId.contains(b)：删除这个Pair
         * 或者 childNodesIdOfHoleNodes.contains(a) || childNodesIdOfHoleNodes.contains(b)：删除这个Pair
         * 或者 (beginNodeParentId, holeBeginNode)、(holeEndNode, NextNodeOfEndNode)在c边集合中，删除这个Pair
         */
        Pair<String, String> finalBeginEdge = beginEdge;
        Pair<String, String> finalEndEdge = endEdge;

        // 过滤控制流边中，和窟窿中节点相关的边
        List<Pair<String, String>> controlFlowPairs = this.edgeMap.get("c").stream()
                .filter(edge -> (!holeNodesId.contains(edge.a) || !holeNodesId.contains(edge.b))
                        && !childNodesIdOfHoleNodes.contains(edge.a)
                        && !childNodesIdOfHoleNodes.contains(edge.b)
                        && !edge.equals(finalBeginEdge)
                        && !edge.equals(finalEndEdge))
                .collect(Collectors.toList());
        this.edgeMapWithHoleNode.put("c", controlFlowPairs);

        /*
         * 3.2.3 删除控制数据流 cd边 集合中，所有和窟窿中节点有关联的边
         * 对于cd边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) && holeNodesId.contains(b)：删除这个Pair
         * 或者 childNodesIdOfHoleNodes.contains(a) || childNodesIdOfHoleNodes.contains(b)：删除这个Pair
         * 或者 (beginNodeParentId, holeBeginNode)、(holeEndNode, NextNodeOfEndNode)在c边集合中，删除这个Pair
         */
        List<Pair<String, String>> controlFlowAndDataFlowPairs = this.edgeMap.get("cd").stream()
                .filter(edge -> (!holeNodesId.contains(edge.a) || !holeNodesId.contains(edge.b))
                        && !childNodesIdOfHoleNodes.contains(edge.a)
                        && !childNodesIdOfHoleNodes.contains(edge.b)
                        && !edge.equals(finalBeginEdge)
                        && !edge.equals(finalEndEdge))
                .collect(Collectors.toList());
        this.edgeMapWithHoleNode.put("cd", controlFlowAndDataFlowPairs);

        /*
         * 3.2.4 删除数据流 d边 集合中，所有和窟窿中节点有关联的边
         * 对于d边集合中的每一个Pair(a,b)
         * 如果 holeNodesId.contains(a) || holeNodesId.contains(b) || childNodesIdOfHoleNodes.contains(a) || childNodesIdOfHoleNodes.contains(b)
         * 那么删除这个Pair
         */
        List<Pair<String, String>> dataFlowPairs = this.edgeMap.get("d").stream()
                .filter(edge -> !holeNodesId.contains(edge.a)
                        && !holeNodesId.contains(edge.b)
                        && !childNodesIdOfHoleNodes.contains(edge.a)
                        && !childNodesIdOfHoleNodes.contains(edge.b))
                .collect(Collectors.toList());
        this.edgeMapWithHoleNode.put("d", dataFlowPairs);
    }

    /**
     * 4.生成一行数据
     * @param holeSize
     * @param holeBeginNode
     */
    public void saveData(int holeSize, GraphNode holeBeginNode){
        // 4.1 记录Graph Vocab
        DataCollector.graphVocabList.add(this.getGraphVocab());
        // 4.2 记录Graph Representation
        DataCollector.graphReprensentList.add(this.getGraphRepresentation());
        // 4.3 记录trace
        DataCollector.traceList.add(this.methodName + " (" + this.fileName + ")");
        // 4.4 记录hole size
        DataCollector.holeSizeList.add(String.valueOf(holeSize));
        // 4.5 记录beginNode的name，得到prediction
        DataCollector.singlePredictionList.add(holeBeginNode.getNodeName());
        // 4.6 记录block prediction
        DataCollector.blockPredictionList.add(this.getBlockPrediction(holeBeginNode));
        // 4.7 记录variable name
        DataCollector.variableNameList.add(this.getVariableNames());
        // 4.8 记录prediction对应的原始语句
        DataCollector.originalStatementList.add(this.getOriginalStatements(holeBeginNode));
        // 4.9 记录分词后的method name
        DataCollector.methodNameList.add(this.getMethodNames());
    }

    /**
     * 4.1 生成Graph Vocab, 并保存id的映射到idChangeMap
     */
    public List<String> getGraphVocab(){
        /*
         * 创建一个旧id到新id的映射
         * 并生成Graph Vocab
         */
        this.idChangeMap = new HashMap<>();
        List<String> graphVocab = new ArrayList<>();

        for(int i = 0; i < this.graphNodeListWithHole.size(); i++){
            this.idChangeMap.put(this.graphNodeListWithHole.get(i).getId(), String.valueOf(i + 1));
            graphVocab.add((i + 1) + ":" + '\'' + this.graphNodeListWithHole.get(i).getNodeName() + '\'');
        }

        return graphVocab;
    }

    /**
     * 4.2 利用idChangeMap生成Graph Representation
     */
    public List<String> getGraphRepresentation(){

        List<String> formatEdges = new ArrayList<>();

        formatEdges.addAll(this.changeEdgeId("c", "1"));
        formatEdges.addAll(this.changeEdgeId("d", "2"));
        formatEdges.addAll(this.changeEdgeId("cd", "3"));
        formatEdges.addAll(this.changeEdgeId("s", "4"));

        return formatEdges;
    }

    /**
     * 更改节点的id，并生成某种边对应的Graph Representation
     * @param edgeType c d cd s
     * @param edgeTypeNum 1 2 3 4
     * @return
     */
    public List<String> changeEdgeId(String edgeType, String edgeTypeNum){
        List<String> formatEdges = new ArrayList<>();
        for(Pair<String, String> edge : this.edgeMapWithHoleNode.get(edgeType)){
            String formatEdge = "[" +
                    this.idChangeMap.get(edge.a) +
                    "," +
                    edgeTypeNum +
                    "," +
                    this.idChangeMap.get(edge.b) +
                    "]";
            formatEdges.add(formatEdge);
        }
        return formatEdges;
    }

    /**
     * 4.6 获取block prediction
     * @param holeBeginNode
     * @return
     */
    public String getBlockPrediction(GraphNode holeBeginNode){

        // 对beginNode进行深度优先遍历，只保留在holeNodes和deletedChildNodes中的节点, 这些节点作为blockPrediciton
        // 这样可以保证blockPrediciton里的节点都是按深度优先顺序的
        this.holeNodes.addAll(this.childNodesOfHoleNodes);

        Graph graph = new Graph();
        List<GraphNode> blockPredictionNodes = graph.getGraphNodesDFS(holeBeginNode);
        blockPredictionNodes.retainAll(this.holeNodes);

        StringBuilder blockPredicitonStr = new StringBuilder();
        for(GraphNode holeNode : blockPredictionNodes){
            blockPredicitonStr.append(holeNode.getNodeName()).append(" ");
        }

        return blockPredicitonStr.toString();
    }

    /**
     * 4.7 获取graphNodeListWithHole集合中，所有变量的名字，并分词
     * @return
     */
    public String getVariableNames() {

        List<String> splitVariableNames = this.graphNodeListWithHole.stream()
                .filter(graphNode -> graphNode.getVarIdentifier() != null && !graphNode.getVarIdentifier().isEmpty())
                .map(GraphNode::getVarIdentifier)
                .map(variableName -> StringUtil.getSplitVariableNameList(variableName, FileUtil.gloveVocabList, FileUtil.stopWordsList))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return String.join(" ", splitVariableNames);
    }

    /**
     * 4.8 获取prediction对应的原始语句
     * @param holeBeginNode
     * @return
     */
    public String getOriginalStatements(GraphNode holeBeginNode) {
//        if(holeBeginNode.getOriginalStatement() != null && !holeBeginNode.getOriginalStatement().isEmpty())
//            return holeBeginNode.getOriginalStatement();
//        else
//            return "";
        return holeBeginNode.getOriginalStatement();
    }

    /**
     * 4.9 获取分词后的方法名
     * @return
     */
    public String getMethodNames(){
        List<String> splitMethodNames = StringUtil.getSplitVariableNameList(this.methodName, FileUtil.gloveVocabList, FileUtil.stopWordsList);
        return String.join(" ", splitMethodNames);
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
