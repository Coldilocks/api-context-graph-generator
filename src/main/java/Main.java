import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Pair;
import dataset.DataCollector;
import dataset.HoleCreator;
import entity.Graph;
import entity.GraphNode;
import util.FileUtil;
import util.GraphvizUtil;
import util.StringUtil;
import visitors.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class Main {

    private static String fileListPath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/testcase/file_list.txt";

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

        boolean isCreateGraph = true;
        boolean isCreateDataset = false;

        String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {

                    System.out.println(method.getNameAsString());

                    Graph graph = new Graph();

                    MethodVisitor visitor = new MethodVisitor(graph);
                    // get graph node of current method, graphNodes list only have one element, i.e. root node.
                    List<GraphNode> graphNodes = method.accept(visitor, "");

                    /*
                     * 1. Print all node names
                     */
                    // visitor.nodeNameList.forEach(System.out::println);

                    /*
                     * 2. Get root node
                     */
                    GraphNode rootNode = graphNodes.get(0);
                    // graph.depthFirstTraversal(rootNode);

                    /*
                     * 3. Get nodes in depth-first order
                     */
                    // graph.breadthFirstTraversal(rootNode).forEach(node -> System.out.println(node.getNodeInfo()));
                    List<GraphNode> graphNodeList = graph.getGraphNodesDFS(rootNode);

                    /*
                     * 4. Get data and control flow edge, and create a graph
                     */
                    Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode);

                    if(isCreateGraph){
                        try {
                            GraphvizUtil.createGraphWithColor("/Users/coldilock/Downloads/output/graph/" + StringUtil.getUuid() +".dot", graphNodeList, edgeMap);
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
                }));
    }
}
