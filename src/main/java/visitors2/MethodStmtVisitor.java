package visitors2;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author coldilock
 * MethodStmtVisitor
 */
public class MethodStmtVisitor extends VoidVisitorAdapter<Graph> {
    @Override
    public void visit(AssertStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(BlockStmt n, Graph graph) {
        System.out.println("BLOCK");
        super.visit(n, graph);
    }

    @Override
    public void visit(BreakStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ContinueStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(DoStmt n, Graph graph) {
        System.out.println("[DO WHILE] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(EmptyStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ExpressionStmt n, Graph graph) {
        System.out.println("[EXPRESSION] " + n.toString());
        Expression expression = n.getExpression();
        if(expression != null){
            if(expression.isVariableDeclarationExpr()){
                System.out.println("this is a variable declaration expression: " + expression.toString());
            } else if (expression.isMethodCallExpr()){
                System.out.println("this is a method call expression: " + expression.toString());
            } else if (expression.isAssignExpr()){
                System.out.println("this is a assign call expression: " + expression.toString());
            }
        }
        super.visit(n, graph);
    }

//    @Override
//    public void visit(AssignExpr n, Graph arg) {
//        System.out.println("this is a assign expression: " + n.toString());
//        super.visit(n, arg);
//    }
//
//    @Override
//    public void visit(BinaryExpr n, Graph arg) {
//        System.out.println("this is a binary expression: " + n.toString());
//        super.visit(n, arg);
//    }
//
//    @Override
//    public void visit(MethodCallExpr n, Graph arg) {
//        System.out.println("this is a method call expression: " + n.toString());
//        super.visit(n, arg);
//    }

    @Override
    public void visit(ForEachStmt n, Graph graph) {
        System.out.println("[FOR EACH] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(ForStmt n, Graph graph) {
        System.out.println("[FOR] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(IfStmt n, Graph graph) {
        System.out.println("[IF] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ReturnStmt n, Graph graph) {
        System.out.println("[RETURN] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(SwitchStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(SynchronizedStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ThrowStmt n, Graph graph) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(TryStmt n, Graph graph) {
        System.out.println("[TRY] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(WhileStmt n, Graph graph) {
        System.out.println("[WHILE] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(UnparsableStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(YieldStmt n, Graph graph) {
        super.visit(n, graph);
    }
}
