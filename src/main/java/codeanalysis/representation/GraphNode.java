package codeanalysis.representation;

import com.github.javaparser.ast.stmt.Statement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class GraphNode implements Cloneable, Serializable {
    /** direct parent node from control flow */
    private GraphNode parentNode;
    /** child nodes */
    private List<GraphNode> childNodes;
    /** linked parent nodes from data flow */
    private List<GraphNode> linkedNodes;
    /** record the original statement of current node */
    private String originalStatement;
    /** name of the node */
    private String nodeName;
    /** javaparser statement node */
    private Statement japaStatement;
    /** node type **/
    private String nodeType;
    /** unique identifier **/
    private String id;

    /** variable identifier **/
    private String varIdentifier;

    public GraphNode(){
        this.childNodes = new ArrayList<>();
        this.linkedNodes = new ArrayList<>();
    }

    public GraphNode(String nodeName){
        this.childNodes = new ArrayList<>();
        this.linkedNodes = new ArrayList<>();
        this.nodeName = nodeName;
    }

    public GraphNode(String nodeName, String id){
        this.childNodes = new ArrayList<>();
        this.linkedNodes = new ArrayList<>();
        this.nodeName = nodeName;
        this.id = id;
    }

//    public GraphNode(String nodeName, String nodeType){
//        this.childNodes = new ArrayList<>();
//        this.linkedNodes = new ArrayList<>();
//        this.nodeName = nodeName;
//        this.nodeType = nodeType;
//    }

//    public GraphNode(String nodeName, String varIdentifier, String nodeType, String originalStatement){
//        this.childNodes = new ArrayList<>();
//        this.linkedNodes = new ArrayList<>();
//        this.nodeName = nodeName;
//        this.varIdentifier = varIdentifier;
//        this.nodeType = nodeType;
//        this.originalStatement = originalStatement;
//    }

    public GraphNode(String nodeName, String varIdentifier, String nodeType, String originalStatement, String id){
        this.childNodes = new ArrayList<>();
        this.linkedNodes = new ArrayList<>();
        this.nodeName = nodeName;
        this.varIdentifier = varIdentifier;
        this.nodeType = nodeType;
        this.originalStatement = originalStatement;
        this.id = id;
    }

    public GraphNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(GraphNode parentNode) {
        this.parentNode = parentNode;
    }

    public List<GraphNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<GraphNode> childNodes) {
        this.childNodes = childNodes;
    }

    public List<GraphNode> getLinkedNodes() {
        return linkedNodes;
    }

    public void setLinkedNodes(List<GraphNode> linkedNodes) {
        this.linkedNodes = linkedNodes;
    }

    public String getOriginalStatement() {
        return originalStatement;
    }

    public void setOriginalStatement(String originalStatement) {
        this.originalStatement = originalStatement;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Statement getJapaStatement() {
        return japaStatement;
    }

    public void setJapaStatement(Statement japaStatement) {
        this.japaStatement = japaStatement;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getVarIdentifier() {
        return varIdentifier;
    }

    public void setVarIdentifier(String varIdentifier) {
        this.varIdentifier = varIdentifier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addChildNode(GraphNode graphNode){
        this.childNodes.add(graphNode);
    }

    public void traversalTree(GraphNode graphNode){
        if(graphNode == null)
            return;

        System.out.println(graphNode.getNodeInfo());

        if(graphNode.getChildNodes() != null){
            graphNode.getChildNodes().forEach(this::traversalTree);
        }
    }

    public String getNodeInfo() {
        return "GraphNode{" +
                " id='" + id + '\'' +
                "   nodeName='" + nodeName + '\'' +
                "   originalStatement= '" + originalStatement + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "nodeName='" + nodeName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
