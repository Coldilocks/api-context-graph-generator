package visitor;

import codeanalysis.constructor.GraphConstructor;
import codeanalysis.representation.Graph;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import util.DataConfig;
import visitors.*;
import visitors2.MethodStmtVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class MultiVisitorTest {

    private static String filePath = "src/test/resources/testcase/Method1-8.java";


    public static void main(String[] args) throws FileNotFoundException {

        // run(args);

        // get import list with or without asterisk
        // testForImport();

        // get all field of the current file
        // testForFieldDeclaration();

        // run method visitor
        // testForMethod();

        // run method statement visitor
        // testForMethodStatement();

        // visit and resolve type for method call
        jdkMethodVisitorAndSymbolSolver();

    }

    public static void run(String[] args) throws FileNotFoundException {
        if(args.length == 0){
            System.out.println("please specify the path of the config file");
            System.exit(0);
        } else if(!new File(args[0]).exists()){
            System.out.printf("%s doesn't exist", args[0]);
            System.exit(0);
        } else {
            DataConfig.loadConfig(args[0]);
        }

        String filePath = DataConfig.TEST_INPUT_JAVA_FILE;

        GraphConstructor graphConstructor = new GraphConstructor();
        graphConstructor.createGraph(filePath);

    }

    /**
     * get import list with or without asterisk
     * @throws FileNotFoundException
     */
    public static void testForImport() throws FileNotFoundException {

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        Map<String, List<String>> importNames = new HashMap<>();
        VoidVisitor<Map<String, List<String>>> importVisitor = new ImportVisitor();
        importVisitor.visit(cu, importNames);

        for(Map.Entry<String, List<String>> entry : importNames.entrySet()){
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }

    }

    /**
     * get all fields of class
     * @throws FileNotFoundException
     */
    public static void testForFieldDeclaration() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        List<VariableDeclarationExpr> filedDeclarations = new ArrayList<>();
        VoidVisitor<List<VariableDeclarationExpr>> fieldVisitor = new FieldVisitor();
        fieldVisitor.visit(cu, filedDeclarations);

        System.out.println(filedDeclarations);

    }

    /**
     * method visitor
     * @throws FileNotFoundException
     */
    public static void testForMethod() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    System.out.println("<<<<<<<<<<");
                    Graph graph = new Graph();
                    MethodVisitor methodVisitor = new MethodVisitor();
                    method.accept(methodVisitor, graph);
//                    System.out.println(graph);

                    System.out.println(">>>>>>>>>>");
                })
        );



    }

    /**
     * method statement visitor
     * @throws FileNotFoundException
     */
    public static void testForMethodStatement() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        cu.getTypes().forEach(type ->
                        type.getMethods().forEach(method -> {
                            System.out.println("<<<<<<<<<<");
                            Graph graph = new Graph();
                            MethodStmtVisitor methodStmtVisitor = new MethodStmtVisitor();
                            method.accept(methodStmtVisitor, graph);
                            // System.out.println(graph);

                            System.out.println(">>>>>>>>>>");
                        })
        );
    }

    /**
     * resolve type of JDK method call or variable declaration
     * @throws FileNotFoundException
     */
    public static void jdkMethodVisitorAndSymbolSolver() throws FileNotFoundException {
        TypeSolver typeSolver = new ReflectionTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        MethodVisitor methodVisitor = new MethodVisitor();
        cu.accept(methodVisitor, new Graph());

//        cu.findAll(MethodCallExpr.class).forEach(mce ->
//                System.out.println(mce.resolve().getQualifiedSignature())
//        );

    }










}
