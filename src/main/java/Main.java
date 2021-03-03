import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Pair;
import dataset.DataCollector;
import dataset.HoleCreator;
import entity.Graph;
import entity.GraphNode;
import util.FileUtil;
import util.GraphvizUtil;
import visitors.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author coldilock
 */
public class Main {

    private static String fileListPath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/testcase/file_list.txt";

    private static boolean isCreateGraph = true;
    private static boolean isCreateDataset = true;

    private static boolean checkJdkAPI = true;
    private static boolean checkThirdPartyAPI = true;
    private static boolean checkUserDefinedAPI = true;


    private static String currentMethodName = "";



    public static void main(String[] args) throws Exception {
        FileUtil.initVocab();
        DataCollector.createWriters();
        run(fileListPath);
        DataCollector.closeWriters();
    }

    public static void run(String fileListPath) throws Exception {
        List<String> filePathList = FileUtil.readFile2List(fileListPath);
        for(String filePath : filePathList){
            System.out.println("当前处理的文件："+filePath);
            getControlAndDataFlow(filePath);
        }
    }

    public static void getControlAndDataFlow(String filePath) throws Exception {

        String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";

        String secondJarFile = "/Users/coldilock/.m2/repository/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar";
        String projectSrcPath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/main/java";

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();

        if(checkJdkAPI)
            typeSolver.add(new ReflectionTypeSolver());
        if(checkThirdPartyAPI){
            typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));
            typeSolver.add(JarTypeSolver.getJarTypeSolver(secondJarFile));
        }

        if(checkUserDefinedAPI)
            typeSolver.add(new JavaParserTypeSolver(new File(projectSrcPath)));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));


        cu.getTypes().forEach(type -> {

            int totalMethodCount = type.getMethods().size();

            AtomicInteger currentMethodCound = new AtomicInteger();

            type.getMethods().forEach(method -> {
                currentMethodName = method.getNameAsString();

                Graph graph = new Graph();

                MethodVisitor visitor = new MethodVisitor(graph);
                // get graph node of current method, graphNodes list only have one element, i.e. root node.
                List<GraphNode> graphNodes = method.accept(visitor, "");

                if(graphNodes == null || graphNodes.isEmpty())
                    return;

                /*
                 * 1. Get root node
                 */
                GraphNode rootNode = graphNodes.get(0);
                // graph.depthFirstTraversal(rootNode);

                /*
                 * 2. Get nodes in depth-first order
                 */
                // graph.breadthFirstTraversal(rootNode).forEach(node -> System.out.println(node.getNodeInfo()));
                List<GraphNode> graphNodeList = graph.getGraphNodesDFS(rootNode);

                /*
                 * 3. Print all node names
                 */
                // graphNodeList.forEach(graphNode -> System.out.println(graphNode.getNodeName()));

                /*
                 * 4. Get data and control flow edge, and create a graph
                 */
                Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode);

                if(isCreateGraph){
                    try {
                        GraphvizUtil.createGraphWithColor("/Users/coldilock/Downloads/output/graph/" + currentMethodName +".dot", graphNodeList, edgeMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*
                 * 5. make hole and create dataset
                 */
                if(isCreateDataset){
                    HoleCreator holeCreator = new HoleCreator(graphNodeList, edgeMap, filePath, method.getNameAsString());
                    holeCreator.createHole();
                    // DataCollector.createDataSet();
                    DataCollector.saveCurrentData();
                }

                currentMethodCound.getAndIncrement();
                System.out.println(currentMethodCound + "/" + totalMethodCount + ": " +method.getNameAsString());
            });

        });

//        cu.getTypes().forEach(type ->
//                type.getMethods().forEach(method -> {
//
//                    System.out.println("当前方法名："+method.getNameAsString());
//                    currentMethodName = method.getNameAsString();
//
//                    Graph graph = new Graph();
//
//                    MethodVisitor visitor = new MethodVisitor(graph);
//                    // get graph node of current method, graphNodes list only have one element, i.e. root node.
//                    List<GraphNode> graphNodes = method.accept(visitor, "");
//
//                    /*
//                     * 1. Get root node
//                     */
//                    GraphNode rootNode = graphNodes.get(0);
//                    // graph.depthFirstTraversal(rootNode);
//
//                    /*
//                     * 2. Get nodes in depth-first order
//                     */
//                    // graph.breadthFirstTraversal(rootNode).forEach(node -> System.out.println(node.getNodeInfo()));
//                    List<GraphNode> graphNodeList = graph.getGraphNodesDFS(rootNode);
//
//                    /*
//                     * 3. Print all node names
//                     */
//                    // graphNodeList.forEach(graphNode -> System.out.println(graphNode.getNodeName()));
//
//                    /*
//                     * 4. Get data and control flow edge, and create a graph
//                     */
//                    Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode);
//
//                    if(isCreateGraph){
//                        try {
//                            GraphvizUtil.createGraphWithColor("/Users/coldilock/Downloads/output/graph/" + currentMethodName +".dot", graphNodeList, edgeMap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    /*
//                     * 5. make hole and create dataset
//                     */
//                    if(isCreateDataset){
//                        HoleCreator holeCreator = new HoleCreator(graphNodeList, edgeMap, filePath, method.getNameAsString());
//                        holeCreator.createHole();
//                        // DataCollector.createDataSet();
//                        DataCollector.saveCurrentData();
//                    }
//                }));
    }
}
