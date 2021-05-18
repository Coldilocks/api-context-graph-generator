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
import util.DataConfig;
import util.FileUtil;
import util.GraphvizUtil;
import visitors.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author coldilock
 */
public class Main {

    private static boolean isCreateGraph = true;
    private static boolean isCreateDataset = true;

    private static boolean checkJdkAPI = true;
    private static boolean checkThirdPartyAPI = true;
    private static boolean checkUserDefinedAPI = false;

    public static void main(String[] args) throws Exception {

        if(args.length == 0){
            System.out.println("Please specify the path of the config file");
        } else if(!new File(args[0]).exists()){
            System.out.printf("%s doesn't exist", args[0]);
        } else {
            DataConfig.loadConfig(args[0]);

            FileUtil.getAllJavaFileList(DataConfig.DATASET_ROOT, DataConfig.JAVA_FILE_PATH);
            FileUtil.getAllJarFileList(DataConfig.JAR_FILE_ROOT, DataConfig.JAR_FILE_PATH);
            FileUtil.initVocab();

            DataCollector.createWriters();
            createDataset(DataConfig.JAVA_FILE_PATH);
            DataCollector.closeWriters();
        }
    }

    public static void createDataset(String fileListPath) throws Exception {
        List<String> filePathList = FileUtil.readFile2List(fileListPath);
        for(String filePath : filePathList){
            System.out.printf("Currently processing: %s\n", filePath);
            getControlAndDataFlow(filePath);
        }
    }

    public static void getControlAndDataFlow(String filePath) throws Exception {

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();

        // solve qualified name from jdk api
        if(checkJdkAPI){
            typeSolver.add(new ReflectionTypeSolver());
        }
        // solve qualified name from third party api
        if(checkThirdPartyAPI){
            // get the jar file list
            List<String> jarFileList = FileUtil.readFile2List(DataConfig.JAR_FILE_PATH);

            for(String jarFilePath : jarFileList){
                typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFilePath));
            }
        }
        // solve qualified name from user-defined class and method
        if(checkUserDefinedAPI){
            // add the source root path of filePath
            String projectSrcPath = "src/main/java";

            typeSolver.add(new JavaParserTypeSolver(new File(projectSrcPath)));
        }

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.getTypes().forEach(type -> {

            int totalMethodCount = type.getMethods().size();

            AtomicInteger currentMethodCound = new AtomicInteger();

            type.getMethods().forEach(method -> {

                Graph graph = new Graph();

                MethodVisitor visitor = new MethodVisitor(graph);
                // get graph node of current method, graphNodes list only have one element, i.e. root node.
                List<GraphNode> graphNodes = method.accept(visitor, "");

                if(graphNodes == null || graphNodes.isEmpty())
                    return;

                // 1. Get root node
                GraphNode rootNode = graphNodes.get(0);

                // 2. Get nodes and id in depth-first order
                List<GraphNode> graphNodeList = graph.getGraphNodesDFS(rootNode);

                List<String> graphNodeIdList = graphNodeList.stream().map(GraphNode::getId).collect(Collectors.toList());

                // 3. Print all node names
                // graphNodeList.forEach(graphNode -> System.out.println(graphNode.getNodeName()));

                // 4. Get data and control flow edge, and create a graph
                Map<String, List<Pair<String, String>>> edgeMap = graph.getControlAndDataFlowPairs(rootNode, graphNodeIdList);

                if(isCreateGraph){
                    try {
                        GraphvizUtil.createGraphWithColor(DataConfig.GRAPH_OUTPUT_PATH + method.getNameAsString() +".dot", graphNodeList, edgeMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 5. make hole and create dataset
                if(isCreateDataset){
                    HoleCreator holeCreator = new HoleCreator(graphNodeList, edgeMap, filePath, method.getNameAsString());
                    holeCreator.createHole();
                    DataCollector.saveCurrentData();
                }

                currentMethodCound.getAndIncrement();
                System.out.printf("%d/%d : %s\n", currentMethodCound.get(), totalMethodCount, method.getNameAsString());
            });

        });
    }
}
