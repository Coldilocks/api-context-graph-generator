package visitor.tutorial;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * @author coldilock
 */
public class MyGenericVisitor extends GenericVisitorAdapter<List<String>, Void> {
    @Override
    public List<String> visit(final ClassOrInterfaceDeclaration n, final Void arg) {
        // look into members
        List<String> nestedClassNames = super.visit(n, arg);
        // accumulate the class name
        nestedClassNames.add(n.getNameAsString());
        return nestedClassNames;
    }

    private static class Visitorr extends VoidVisitorAdapter<Void>{
        @Override
        public void visit(SwitchStmt n, Void arg) {
            System.out.println("Found a switch inside the method");
        }

    }

    public static void main(String[] args) {
        CompilationUnit cu = StaticJavaParser.parse("class X {void x (){switch (1){}} int y(){switch (1){}}}");
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
                    Visitorr visitorr = new Visitorr();
                    method.accept(visitorr, null);
                })
        );
    }
}
