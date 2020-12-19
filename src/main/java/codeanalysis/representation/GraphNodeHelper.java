package codeanalysis.representation;


import com.github.javaparser.ast.stmt.Statement;

/**
 * @author coldilock
 */
public class GraphNodeHelper {
    private String nodeName;
    private String nodeType;
    private Statement currentStmt;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Statement getCurrentStmt() {
        return currentStmt;
    }

    public void setCurrentStmt(Statement currentStmt) {
        this.currentStmt = currentStmt;
    }
}
