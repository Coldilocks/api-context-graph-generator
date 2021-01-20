package visitor;

import codeanalysis.constructor.GraphConstructor;
import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.TreeVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Pair;
import util.DataConfig;
import util.GraphvizUtil;
import visitors.*;
import visitors2.MethodCompleteVisitor;
import visitors2.MethodStmtVisitor;
import visitorsz.MethodVisitorZ;
//import visitorsx.MethodVisitorX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author coldilock
 */
public class MultiVisitorTest {

    private static String filePath = "src/test/resources/testcase/Task1.java";


    public static void main(String[] args) throws IOException {

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
        // jdkMethodVisitorAndSymbolSolver();

        // using MethodCompleteVisitor
        // getJdkAndCustomType();

        // using MethodGenericVisitor
//        getControlFlow();

        // using MethodGenericVisitorZ
        getControlFlowZ();

        // pre order the tree
        // preOrder();
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

    /**
     * print all the qualified name for variable, method call
     * @throws IOException
     */
    public static void getJdkAndCustomType() throws IOException {

        String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    System.out.println("<<<<<<<< START >>>>>>>>");
                    Graph graph = new Graph();

                    MethodCompleteVisitor visitor = new MethodCompleteVisitor();
                    method.accept(visitor, graph);

                    System.out.println("<<<<<<<< END >>>>>>>>");

                    System.out.println();

                    visitor.nodeNameList.forEach(System.out::println);

                }));



    }


    /**
     * get the control flow graph for a method
     * @throws IOException
     */
//    public static void getControlFlow() throws IOException {
//
//        String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";
//
//        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
//        typeSolver.add(new ReflectionTypeSolver());
//        typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));
//
//        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
//        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
//
//        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
//
//        cu.getTypes().forEach(type ->
//                type.getMethods().forEach(method -> {
//                    System.out.println("\n<<<<<<<< 1.Visiting >>>>>>>>\n");
//                    Graph graph = new Graph();
//
//                    MethodVisitorX visitor = new MethodVisitorX();
//                    // get graph node of current method, graphNodes list only have one element, i.e. root node.
//                    List<GraphNode> graphNodes = method.accept(visitor, graph);
//
//                    System.out.println("\n<<<<<<<< 2.Nodes in List >>>>>>>>\n");
//
//                    visitor.nodeNameList.forEach(System.out::println);
//
//                    System.out.println("\n<<<<<<<< 3.Nodes in Tree >>>>>>>>\n");
//
//                    GraphNode rootNode = graphNodes.get(0);
//                    rootNode.traversalTree(rootNode);
//
//                    System.out.println("\n<<<<<<<< 4.Data dependency >>>>>>>>\n");
//
//                    graph.getDataFlowMatrix().forEach(System.out::println);
//
//
//                }));
//
//
//
//    }

    public static void getControlFlowZ() throws IOException {

        String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    Graph graph = new Graph();

                    MethodVisitorZ visitor = new MethodVisitorZ(graph);
                    // get graph node of current method, graphNodes list only have one element, i.e. root node.
                    List<GraphNode> graphNodes = method.accept(visitor, "");

                    System.out.println("\n<<<<<<<< 1. Node Names in List >>>>>>>>\n");

                    visitor.nodeNameList.forEach(System.out::println);

                    System.out.println("\n<<<<<<<< 2. Nodes in Depth-First Order >>>>>>>>\n");

                    GraphNode rootNode = graphNodes.get(0);
                    graph.depthFirstTraversal(rootNode);

                    System.out.println("\n<<<<<<<< 3. Nodes in Breadth-First Order >>>>>>>>\n");

                    // graph.breadthFirstTraversal(rootNode).forEach(node -> System.out.println(node.getNodeInfo()));
                    List<GraphNode> graphNodeList = graph.breadthFirstTraversal(rootNode);

                    System.out.println("\n<<<<<<<< 4. Data and Control Flow Edge >>>>>>>>\n");
                    Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode);

                    try {
                        GraphvizUtil.createGraph("/Users/coldilock/Downloads/first_result.dot", graphNodeList, edgeMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }));



    }


    private static void preOrder() throws FileNotFoundException {

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    System.out.println("<<<<<<<< START >>>>>>>>");
                    new TreeVisitor() {
                        @Override
                        public void process(Node node) {
//                            System.out.println("[[[["+node.toString()+"]]]]\n");
                        }
                    }.visitPreOrder(method);

                    System.out.println("<<<<<<<< END >>>>>>>>");

                }));


    }








}
