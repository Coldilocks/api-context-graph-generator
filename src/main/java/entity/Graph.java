package entity;

import com.github.javaparser.utils.Pair;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>Graph Creator</h1>
 * This class includes several useful methods to create graph.
 *<ul>
 *     <li>Link separated nodes to create a control flow tree structure when visiting statements</li>
 *     <li>Maintain a Symbol Table and New Variable Defined In Current Scope List to link dataflow</li>
 *     <li>Breadth and depth first traversal of the AST, get all nodes inside it</li>
 *     <li>Get all control flow edges</li>
 *     <li>Get all c, d and cd edges</li>
 *</ul>
 *
 * @author coldilock
 */
public class Graph {
    /** root node of the method graph */
    private GraphNode rootNode;
    /** last node of the method graph */
    private GraphNode lastNode;
    /**
     * Lookup Table for variables
     *
     * structure:
     *  { var_name : [node1_in_global_scope, node2_in_method_scope, ...] }
     * key: variable name
     * value: all the nodes with the same name, the last node is in the current scope.
     *
     * when the var is defined in this scope, once we jump out of the scope, we should delete the last node in the list.
     **/
    private Map<String, List<String>> symbolTable = new HashMap<>();
    /**
     * New variables in the corresponding scope where they are defined
     *  the first dimension stands for scope, the second dimension stands for variable names defined in this scope
     *  when we jump out of a scope, we should delete the last list in the first dimension, and delete the last node in the lookupTable if their key are in the deleted list.
     *
     *  structure:
     *      [[var1_in_first_scope, var2_in_first_scope, ...], [var1_in_second_scope, var2_in_second_scope, ...], ...]
     **/
    private List<List<String>> newVarInCurrentScope = new ArrayList<>();

    /**
     * 记录每个作用域创建的对象名（通过new来创建的变量），每个list代表一个作用域，最后一个list表示当前作用域
     * 结构:
     *      [[obj_name_1_in_scope_1, obj_name_2_in_scope_1, ...], [obj_name_1_in_scope_2, obj_name_2_in_scope_2, ...], ...]
     **/
    private List<List<String>> newObjInCurrentScope = new ArrayList<>();

    /**
     * 记录每个对象名在不同作用域中所属的runtime类型（如 List<String> list = new ArrayList<>() 中 list的runtime类型是Java.util.ArrayList）
     * 结构:
     *      key: obj_name_i, value: [qualified_class_name_of_obj_i_in_scope_1, qualified_class_name_of_obj_i_in_scope_2, ...]
     * **/
    private Map<String, List<String>> typeTable = new HashMap<>();

    /** Control flow edge pairs, (source_node_id, target_node_id) */
    private List<Pair<String, String>> controlFlowPairs = new ArrayList<>();

    /** Data flow edge pairs, (source_node_id, target_node_id) */
    private List<Pair<String, String>> dataFlowPairs = new ArrayList<>();

    public GraphNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(GraphNode rootNode) {
        this.rootNode = rootNode;
    }

    public GraphNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(GraphNode lastNode) {
        this.lastNode = lastNode;
    }

    public void addNode(GraphNode node){
        if(node == null)
            return;
        if(this.rootNode == null || this.lastNode == null){
            this.rootNode = node;
        } else {
            this.lastNode.addChildNode(node);
            node.setParentNode(this.lastNode);
        }
        this.lastNode = node;
    }

    /**
     * Link nodes if they are on a control flow edge
     * @param rootNode
     * @param graphNodeList
     * @return
     */
    public GraphNode linkNodesInControlFlow(GraphNode rootNode, List<GraphNode> graphNodeList){
        if(graphNodeList == null || Objects.requireNonNull(graphNodeList).size() == 0)
            return rootNode;

        for(int i = 0; i < graphNodeList.size() - 1; i++){
            graphNodeList.get(i).addChildNode(graphNodeList.get(i + 1));
            graphNodeList.get(i + 1).setParentNode(graphNodeList.get(i));

            // 设置next node
            graphNodeList.get(i).setNextNode(graphNodeList.get(i + 1));
        }

        rootNode.addChildNode(graphNodeList.get(0));
        graphNodeList.get(0).setParentNode(rootNode);

        // 设置root node的next node
        rootNode.setNextNode(graphNodeList.get(0));

        return rootNode;
    }

    /**
     * Create a new list and add it to newVarInCurrentScope when we enter into a new scope.
     */
    public void addNewScope(){
        List<String> varScope = new ArrayList<>();
        newVarInCurrentScope.add(varScope);

        List<String> objScope = new ArrayList<>();
        newObjInCurrentScope.add(objScope);
    }

    /**
     * Add the new variable name to current scope in both symbolTable and newVarInCurrentScope.
     * @param varName
     * @param graphNodeId
     */
    public void addNewVarInCurrentScope(String varName, String graphNodeId){
        symbolTable.computeIfAbsent(varName, v -> new ArrayList<>()).add(graphNodeId);
        newVarInCurrentScope.get(newVarInCurrentScope.size() - 1).add(varName);
    }

    /**
     * 分别向 typeTable 和 newObjInCurrentScope 的当前作用域中添加新的对象名和对象所属的runtime类型
     * @param objName
     * @param objQualifiedType
     */
    public void addNewObjInCurrentScope(String objName, String objQualifiedType){
        typeTable.computeIfAbsent(objName, v -> new ArrayList<>()).add(objQualifiedType);
        newObjInCurrentScope.get(newObjInCurrentScope.size() - 1).add(objName);
    }

    /**
     * Delete all the variables defined in the current scope when it is over
     */
    public void jumpOutOfScope(){
        List<String> varNames = newVarInCurrentScope.get(newVarInCurrentScope.size() - 1);
        for(String varName : varNames){
            symbolTable.get(varName).remove(symbolTable.get(varName).size() - 1);
        }
        newVarInCurrentScope.remove(newVarInCurrentScope.size() - 1);

        // 删除 typeTable 和 newObjInCurrentScope 中所有属于当前作用域的对象及类型
        List<String> objNames = newObjInCurrentScope.get(newObjInCurrentScope.size() - 1);
        for(String objName : objNames){
            typeTable.get(objName).remove(typeTable.get(objName).size() - 1);
        }
        newObjInCurrentScope.remove(newObjInCurrentScope.size() - 1);
    }

    /**
     * Search varName in symbolTable to get the id of the node (source node id) in which the variable are newly declared,
     * and create a edge between the source node and target node (currentNodeId).
     *
     * Add (DeclaredNodeId,CurrentNodeId) to controlFlowPairs.
     * @param currentNodeId
     * @param varName
     */
    public void linkDataFlow(String currentNodeId, String varName){
        List<String> nodesIdWithSameVarName = symbolTable.get(varName);
        if(!currentNodeId.isEmpty() && nodesIdWithSameVarName != null){
            String declaredNodeId = nodesIdWithSameVarName.get(nodesIdWithSameVarName.size() - 1);
            if(!declaredNodeId.equals(currentNodeId)){
                dataFlowPairs.add(new Pair<>(declaredNodeId, currentNodeId));
            }
        }
    }

    /**
     * 获取当前作用域中某个对象中的"运行时"类型
     * @param objName
     * @return
     */
    public String getObjQualifiedTypeInCurrentScope(String objName){
        List<String> qualifiedTypesWithSameObjName = typeTable.get(objName);
        if(qualifiedTypesWithSameObjName != null){
            return qualifiedTypesWithSameObjName.get(qualifiedTypesWithSameObjName.size() - 1);
        }
        return null;
    }

    public List<Pair<String, String>> getDataFlowPairs(){
        return dataFlowPairs;
    }

    /**
     * Breadth-first traversal
     * @param root root node of the AST
     * @return all nodes in the AST
     */
    public List<GraphNode> breadthFirstTraversal(GraphNode root){

        List<GraphNode> graphNodeList = new ArrayList<>();

        if(root == null)
            return graphNodeList;

        Queue<GraphNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);

        while(!nodeQueue.isEmpty()){
            GraphNode graphNode = nodeQueue.poll();
            graphNodeList.add(graphNode);
            for(GraphNode childNode : CollectionUtils.emptyIfNull(graphNode.getChildNodes())){
                nodeQueue.offer(childNode);
            }
        }

        return graphNodeList;
    }

    /**
     * Get nodes in Depth-first order
     * @param root root node of the AST
     */
    public List<GraphNode> getGraphNodesDFS(GraphNode root){
        List<GraphNode> graphNodeList = new ArrayList<>();
        depthFirstTraversal(root, graphNodeList);
        return graphNodeList;
    }

    public void depthFirstTraversal(GraphNode root, List<GraphNode> graphNodeList){
        if(root == null)
            return;

        graphNodeList.add(root);

        for(GraphNode graphNode : CollectionUtils.emptyIfNull(root.getChildNodes())){
            depthFirstTraversal(graphNode, graphNodeList);
        }
    }

    /**
     * Get control flow edge, represented by node pair
     * @param root root node of the AST
     * @return all control flow edges, represented by (source node, target node) pair
     */
    public List<Pair<String, String>> getControlFlow(GraphNode root){

        this.controlFlowPairs =  this.breadthFirstTraversal(root).stream()
                .flatMap(parentNode -> parentNode.getChildNodes().stream()
                        .map(childNode -> new Pair<>(parentNode.getId(), childNode.getId()))
                ).collect(Collectors.toList());

        return this.controlFlowPairs;
    }

    /**
     * Get control flow edge, represented by node pair
     * @param root root node of the AST
     * @return all control flow edges, represented by (source node, target node) pair
     */
    @Deprecated
    public List<Pair<String, String>> getControlFlow2(GraphNode root){
        this.controlFlowPairs = new ArrayList<>();

        List<GraphNode> graphNodes = breadthFirstTraversal(root);

        for(GraphNode graphNode : CollectionUtils.emptyIfNull(graphNodes)){
            for(GraphNode childNode : CollectionUtils.emptyIfNull(graphNode.getChildNodes())){
                controlFlowPairs.add(new Pair<>(graphNode.getId(), childNode.getId()));
            }
        }

        // System.out.println(controlFlowPairs.size());

        return this.controlFlowPairs;
    }

    /**
     * Get data flow edge(d), control flow edge(c), data and control flow edge(cd)
     * @param root root node of the AST
     * @return a map containing c, d, cd edges
     */
    public Map<String, List<Pair<String, String>>> getControlAndDataFlowPairs(GraphNode root, List<String> graphNodeIdList){

        this.getControlFlow(root);

        List<Pair<String, String>> controlFlowAndDataFlowPairs = new ArrayList<>(this.controlFlowPairs);
        controlFlowAndDataFlowPairs.retainAll(this.dataFlowPairs);

        this.controlFlowPairs.removeAll(controlFlowAndDataFlowPairs);

        this.dataFlowPairs.removeAll(controlFlowAndDataFlowPairs);

        // 删除不存在的数据流边（这种情况出现在：变量被定义的结点存在，但是使用变量的结点因为无法解析等原因而不存在）
        this.dataFlowPairs.removeIf(pair -> !graphNodeIdList.contains(pair.a) || !graphNodeIdList.contains(pair.b));

        Map<String, List<Pair<String, String>>> result = new HashMap<>();
        result.put("d", this.dataFlowPairs);
        result.put("c", this.controlFlowPairs);
        result.put("cd", controlFlowAndDataFlowPairs);

        return result;
    }

    public static GraphNode copyGraph(GraphNode rootNode) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(rootNode);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            GraphNode copiedGraph = (GraphNode) ois.readObject();
            return copiedGraph;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
