package visitor.tutorial;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MyGenericVisitor extends GenericVisitorAdapter<List<String>, Void> {

    private static String filePath = "src/test/resources/testcase/Task1.java";

    @Override
    public List<String> visit(BlockStmt n, final Void arg) {
        // look into members
        List<String> result = new ArrayList<>(super.visit(n, arg));
        return result;
    }

    @Override
    public List<String> visit(VariableDeclarationExpr n, final Void arg) {
        List<String> result = new ArrayList<>(super.visit(n, arg));

        result.add(n.toString());

        return result;
    }

    @Override
    public List<String> visit(AssignExpr n, Void arg) {
        List<String> result = new ArrayList<>(super.visit(n, arg));

        result.add(n.toString());

        return result;
    }

    @Override
    public List<String> visit(ObjectCreationExpr n, Void arg) {

        List<String> result = new ArrayList<>(super.visit(n, arg));

        result.add(n.toString());

        return super.visit(n, arg);
    }

//    private static class Visitorr extends VoidVisitorAdapter<Void>{
//        @Override
//        public void visit(SwitchStmt n, Void arg) {
//            System.out.println("Found a switch inside the method");
//        }
//    }

    public static void main(String[] args) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
//        cu.getTypes().forEach(type ->
//                type.getMethods().forEach(method -> {
//                            method.accept(new GenericVisitorAdapter<Node, Void>() {
//                                @Override
//                                public Node visit(SwitchStmt n, Void arg) {
//                                    System.out.println("Found a switch inside the method");
//                                    return n;
//                                }
//                            }, null);
//                        }
//                )
//        );

//        cu.getTypes().forEach(type ->
//                type.getMethods().forEach(method -> {
//                            method.accept(new VoidVisitorAdapter<Void>() {
//                                @Override
//                                public void visit(SwitchStmt n, Void arg) {
//                                    System.out.println("Found a switch inside the method");
//                                }
//                            }, null);
//                        }
//                )
//        );



        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    MyGenericVisitor visitorr = new MyGenericVisitor();
                    System.out.println(method.accept(visitorr, null));
                })
        );
    }
}
