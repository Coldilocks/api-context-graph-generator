package codeanalysis.representation;

import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author coldilock
 */
public class Graph {
    /** root node of the method graph */
    private GraphNode rootNode;
    /** last node of the method graph */
    private GraphNode lastNode;

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
        if(this.rootNode == null || this.lastNode == null){
            this.rootNode = node;
        } else {
            this.lastNode.addChildNode(node);
            node.setParentNode(this.lastNode);
        }
        this.lastNode = node;
    }
}
