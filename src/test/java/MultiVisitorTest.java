import dataset.DataCollector;
import dataset.HoleCreator;
import entity.Graph;
import entity.GraphNode;
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
import util.FileUtil;
import util.GraphvizUtil;
import util.StringUtil;
import visitor.TestVisitor;
import visitor.visitors1.*;
import visitor.visitors2.MethodCompleteVisitor;
import visitor.visitors2.MethodStmtVisitor;
import visitors.MethodVisitor;
//import visitorsx.MethodVisitorX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author coldilock
 */
public class MultiVisitorTest {

    private static String filePath = "src/test/resources/testcase/Task1.java";

    public static void main(String[] args) throws Exception {
        // run(args);
        // using MethodGVisitor
         getControlAndDataFlow();

        // visitor for a java file
        // testForSpecial();

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



        // pre order the tree
        // preOrder();

        // java.io.File.new(java.lang.String)
        // java.lang.String.String()
        // java.lang.String.new()


//        String str = "java.io.File.File(java.lang.String)";
//        String str = "java.lang.String.String(java.lang.String)";
//
//        System.out.println(StringUtil.replaceName(str));
    }

    public static void getControlAndDataFlow() throws Exception {

        FileUtil.initVocab();

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

                    MethodVisitor visitor = new MethodVisitor(graph);
                    // get graph node of current method, graphNodes list only have one element, i.e. root node.
                    List<GraphNode> graphNodes = method.accept(visitor, "");

                    /*
                     * 1. Node Names in List
                     */
                    visitor.nodeNameList.forEach(System.out::println);

                    /*
                     * 2. Nodes in Depth-First Order
                     */
                    GraphNode rootNode = graphNodes.get(0);
                    // graph.depthFirstTraversal(rootNode);

                    /*
                     * 3. Nodes in Breadth-First Order
                     */
                    // graph.breadthFirstTraversal(rootNode).forEach(node -> System.out.println(node.getNodeInfo()));
                    List<GraphNode> graphNodeList = graph.getGraphNodesDFS(rootNode);

                    /*
                     * 4. Get Data and Control Flow Edge, and Create a Graph
                     */
                    Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode);

                    try {
                        GraphvizUtil.createGraphWithColor("/Users/coldilock/Downloads/" + StringUtil.getUuid() +".dot", graphNodeList, edgeMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*
                     * 5. 构造数据集
                     */
                    HoleCreator holeCreator = new HoleCreator(graphNodeList, edgeMap, filePath, method.getNameAsString());
                    holeCreator.createHole();
                    DataCollector.createDataSet();

                }));



    }

    /**
     * replace the second className to "new"
     * @param originStr e.g. "java.lang.String.String()"
     * @return e.g. "java.lang.String.new()"
     */
    public static String replaceName(String originStr){
        Pattern P = Pattern.compile("([A-Za-z][A-Za-z0-9]*\\.)+([A-Z][A-Za-z0-9]*\\()");

        Matcher m = P.matcher(originStr);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String matchedStr = m.group();
            String needReplaceStr = m.group(2);
            String prefixStr = matchedStr.substring(0, matchedStr.length() - needReplaceStr.length());
            m.appendReplacement(sb, prefixStr + "new(");
        }
        m.appendTail(sb);

        return sb.toString();
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

//        GraphConstructor graphConstructor = new GraphConstructor();
//        graphConstructor.createGraph(filePath);

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
                    visitor.visitors1.MethodVisitor methodVisitor = new visitor.visitors1.MethodVisitor();
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

        visitor.visitors1.MethodVisitor methodVisitor = new visitor.visitors1.MethodVisitor();
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

    /**
     * visitor for a java file
     * @throws FileNotFoundException
     */
    public static void runClazzVisitor() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        TestVisitor x = new TestVisitor();
        cu.accept(x, "");
    }

}
