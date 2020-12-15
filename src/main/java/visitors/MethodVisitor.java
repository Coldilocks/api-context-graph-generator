package visitors;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import visitors2.MethodStmtVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class MethodVisitor extends VoidVisitorAdapter<Graph> {

    private Map<String, String> parameters = new HashMap<>();

    @Override
    public void visit(MethodDeclaration md, Graph graph) {
        /*
         * todo:获取参数
         */
        md.getParameters();

        md.getBody().ifPresent(body -> {
            for(Statement stm : body.getStatements()){
                System.out.println("[STATEMENT]\n" + stm.toString());
                GraphNode node = new GraphNode();
                node.setNodeName(stm.toString());
                node.setOriginalStatement(stm.toString());
                node.setJapaStatement(stm);

                // MethodStmtVisitor methodStmtVisitor = new MethodStmtVisitor();
                // stm.accept(methodStmtVisitor, null);

                graph.addNode(node);
            }
        });

        // this will visit the inner class
        super.visit(md, graph);
    }

    /**
     * example: {@code Integer.valueOf(a).toString().toLowerCase();}
     * if super.visit(n, arg) in the first statement, this method will output:
     *  'java.lang.Integer.valueOf(int)', 'java.lang.Integer.toString()', 'java.lang.String.toLowerCase()';
     * while in the bottom, the output will be reversed
     */
    @Override
    public void visit(MethodCallExpr n, Graph arg) {
        super.visit(n, arg);
        System.out.println(n.resolve().getQualifiedSignature());

    }
}
