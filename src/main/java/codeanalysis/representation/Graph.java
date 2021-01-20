package codeanalysis.representation;

import java.util.*;

/**
 * @author coldilock
 */
public class Graph {
    /** root node of the method graph */
    private GraphNode rootNode;
    /** last node of the method graph */
    private GraphNode lastNode;
    /**
     * lookup table for variables
     *
     * key: variable name
     * value: all the nodes with the same name, the last node is in the latest scope.
     *
     * when the var is defined in this scope, once we jump out of the scope, we should delete the last node in the list.
     *
     * example:
     * var_name : [node1_in_global_scope, node2_in_method_scope, ...]
     **/
    private Map<String, List<String>> symbolTable = new HashMap<>();
    /**
     * new variable in current scope
     * the first dimension stands for scope, the second dimension stands for variable names defined in this scope
     * when we jump out of a scope, we should delete the last list in the first dimension, and delete the last node in the lookupTable of which the key are in the deleted list.
     **/
    private List<List<String>> newVarInCurrentScope = new ArrayList<>();

    private List<List<String>> dataFlowMatrix = new ArrayList<>();

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

    public GraphNode linkGraph(GraphNode rootNode, List<GraphNode> graphNodeList){
        if(graphNodeList == null || Objects.requireNonNull(graphNodeList).size() == 0)
            return rootNode;

        for(int i = 0; i < graphNodeList.size() - 1; i++){
            graphNodeList.get(i).addChildNode(graphNodeList.get(i + 1));
            graphNodeList.get(i + 1).setParentNode(graphNodeList.get(i));
        }

        rootNode.addChildNode(graphNodeList.get(0));
        graphNodeList.get(0).setParentNode(rootNode);

        return rootNode;
    }

    public void addNewScope(){
        List<String> scope = new ArrayList<>();
        newVarInCurrentScope.add(scope);
    }

    public void addNewVarInCurrentScope(String varName, String graphNodeId){
        symbolTable.computeIfAbsent(varName, v -> new ArrayList<>()).add(graphNodeId);
        newVarInCurrentScope.get(newVarInCurrentScope.size() - 1).add(varName);
    }

    public void jumpOutOfScope(){
        List<String> varNames = newVarInCurrentScope.get(newVarInCurrentScope.size() - 1);
        for(String varName : varNames){
            symbolTable.get(varName).remove(symbolTable.get(varName).size() - 1);
        }
        newVarInCurrentScope.remove(newVarInCurrentScope.size() - 1);
    }

    public void linkDataFlow(String currentNodeId, String varName){
        List<String> nodesIdWithSameVarName = symbolTable.get(varName);
        if(!currentNodeId.isEmpty() && nodesIdWithSameVarName != null){
            String declaredNodeId = nodesIdWithSameVarName.get(nodesIdWithSameVarName.size() - 1);
            if(!declaredNodeId.equals(currentNodeId)){
                List<String> temp = new ArrayList<>();
                temp.add(varName);
                temp.add(declaredNodeId);
                temp.add(currentNodeId);
                dataFlowMatrix.add(temp);
            }
        }
    }

    public List<List<String>> getDataFlowMatrix() {
        return dataFlowMatrix;
    }

    private String currentMethodCallNodeId = "";
    private String currentObjCreationNodeId = "";



}
