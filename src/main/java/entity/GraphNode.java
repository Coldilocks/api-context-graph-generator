package entity;

import com.github.javaparser.ast.stmt.Statement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class GraphNode implements Cloneable, Serializable {
    /** unique id **/
    private String id;
    /** variable identifier **/
    private String varIdentifier;
    /** name of the node */
    private String nodeName;
    /** node type **/
    private String nodeType;
    /** direct parent node from control flow */
    private GraphNode parentNode;
    /** child nodes with next node */
    private List<GraphNode> childNodes;
    /** record the original statement of current node */
    private String originalStatement;
    /** javaparser statement node */
    private Statement japaStatement;
    /** next node along the execute flow, not the child node inside the statement of control node*/
    private GraphNode nextNode;

    private List<GraphNode> controlChildNodes;

    private GraphNode controlParentNode;

    public GraphNode(){
        this.childNodes = new ArrayList<>();
        this.controlChildNodes = new ArrayList<>();
    }

    public GraphNode(String nodeName){
        this.childNodes = new ArrayList<>();
        this.controlChildNodes = new ArrayList<>();
        this.nodeName = nodeName;
    }

    public GraphNode(String nodeName, String id){
        this.childNodes = new ArrayList<>();
        this.controlChildNodes = new ArrayList<>();
        this.nodeName = nodeName;
        this.id = id;
    }

    public GraphNode(String nodeName, String nodeType, String originalStatement, String id){
        this.childNodes = new ArrayList<>();
        this.controlChildNodes = new ArrayList<>();
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.originalStatement = originalStatement;
        this.id = id;
    }

    public GraphNode(String nodeName, String varIdentifier, String nodeType, String originalStatement, String id){
        this.childNodes = new ArrayList<>();
        this.controlChildNodes = new ArrayList<>();
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

    public GraphNode getControlParentNode() {
        return controlParentNode;
    }

    public void setControlParentNode(GraphNode controlParentNode) {
        this.controlParentNode = controlParentNode;
    }

    public List<GraphNode> getControlChildNodes() {
        return controlChildNodes;
    }

    public void setControlChildNodes(List<GraphNode> controlChildNodes) {
        this.controlChildNodes = controlChildNodes;
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

    public void addControlChildNode(GraphNode graphNode){
        this.controlChildNodes.add(graphNode);
    }

    public GraphNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(GraphNode nextNode) {
        this.nextNode = nextNode;
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
        String result;
        if(this.varIdentifier != null && !this.varIdentifier.isEmpty()){
            result = "GraphNode{" +
                    "nodeName='" + nodeName + '\'' +
                    "   varName= '" + varIdentifier + '\'' +
                    '}';
        } else {
            result = "GraphNode{" +
                    "nodeName='" + nodeName + '\'' +
                    '}';
        }

        return result;
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
