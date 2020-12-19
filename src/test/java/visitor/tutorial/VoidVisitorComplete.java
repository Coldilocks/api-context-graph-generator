package visitor.tutorial;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 * 在visit的时候收集结果
 */
public class VoidVisitorComplete {

    private static final String FILE_PATH = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/ReversePolishNotation.java";
    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        // A Simple Visitor
//        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
//        methodNameVisitor.visit(cu, null);

        // A Simple Visitor With State
        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);
        methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));
    }

    /** A Simple Visitor */
    private static class MethodNamePrinter extends VoidVisitorAdapter<Void> {

        @Override public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println("Method Name Printed: " + md.getName()); }

    }

    /** A Simple Visitor With State */
    private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

        @Override
        public void visit(MethodDeclaration md, List<String> collector) {

            super.visit(md, collector);

            collector.add(md.getNameAsString()); }

    }


}


