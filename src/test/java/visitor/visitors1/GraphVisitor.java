package visitor.visitors1;

import entity.Graph;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coldilock
 */
public class GraphVisitor extends VoidVisitorAdapter<Graph> {
    private Map<String, String> parameters = new HashMap<>();

    @Override
    public void visit(MethodDeclaration md, Graph graph) {
        md.getBody().ifPresent(body -> {
            for(Statement stm : body.getStatements()){
                System.out.println("STATEMENT");
                System.out.println(stm + "\n");
            }
        });

        super.visit(md, graph);
    }

    @Override
    public void visit(ExpressionStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(VariableDeclarationExpr vde, Graph graph) {
        super.visit(vde, graph);
    }

    @Override
    public void visit(AssignExpr ae, Graph graph) {
        super.visit(ae, graph);
    }

    @Override
    public void visit(BlockStmt bs, Graph graph) {
        super.visit(bs, graph);
    }
}
