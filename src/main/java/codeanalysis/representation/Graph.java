package codeanalysis.representation;

import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Objects;

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
}
