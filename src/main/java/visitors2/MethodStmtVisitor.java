package visitors;

import codeanalysis.representation.Graph;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author coldilock
 * MethodStmtVisitor
 */
public class MethodStmt2 extends VoidVisitorAdapter<Graph> {
    @Override
    public void visit(AssertStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(BlockStmt n, Graph graph) {
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
        super.visit(n, graph);
    }

    @Override
    public void visit(ForEachStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ForStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(IfStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ReturnStmt n, Graph graph) {
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
        super.visit(n, graph);
    }

    @Override
    public void visit(TryStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(WhileStmt n, Graph graph) {
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
