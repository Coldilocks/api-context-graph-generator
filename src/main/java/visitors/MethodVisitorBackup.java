package visitors;

import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class MethodVisitor extends VoidVisitorAdapter<List<String>> {

    private Map<String, String> parameters = new HashMap<>();

    @Override
    public void visit(ExpressionStmt n, List<String> arg) {
        super.visit(n, arg);
    }

    //ExpressionStmt

    @Override
    public void visit(MethodDeclaration md, List<String> node) {
        /*
         * run before visit(VariableDeclarationExpr) and visit(AssignExpr)
         */

//        System.out.println("【method");
        NodeList<Statement> statements =  md.getBody().get().getStatements();
        for(Statement stm : statements){
            System.out.println("statement:");
            System.out.println(stm + "\n");
        }

        super.visit(md, node);

        /*
         * run after visit(VariableDeclarationExpr) and visit(AssignExpr)
         */

//        System.out.println("method】");

        node.add("method declaration");
//        System.out.println("method closed");
    }

    @Override
    public void visit(VariableDeclarationExpr vde, List<String> node) {
//        System.out.println("【variable declaration expression : " + vde.toString());
        super.visit(vde, node);
//        System.out.println("variable declaration expression】 : " + vde.toString());
        node.add("variable declaration expression");
    }

    @Override
    public void visit(AssignExpr ae, List<String> node) {
//        System.out.println("【assign expression : " + ae.toString());
//        node.add("assign expression");

        super.visit(ae, node);

//        System.out.println("assign expression】 : " + ae.toString());
//        node.add("assign expression");
    }

    @Override
    public void visit(BlockStmt bs, List<String> node) {
//        System.out.println("【block" + bs.toString());
        super.visit(bs, node);
//        System.out.println("block】");
    }
}
