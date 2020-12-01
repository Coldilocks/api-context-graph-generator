package codeanalysis.representation;

import java.io.Serializable;
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
    private List<GraphNode> linkedNode;
    /** record the original statement of current node */
    private String originalStatement;

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

    public List<GraphNode> getLinkedNode() {
        return linkedNode;
    }

    public void setLinkedNode(List<GraphNode> linkedNode) {
        this.linkedNode = linkedNode;
    }

    public String getOriginalStatement() {
        return originalStatement;
    }

    public void setOriginalStatement(String originalStatement) {
        this.originalStatement = originalStatement;
    }
}
