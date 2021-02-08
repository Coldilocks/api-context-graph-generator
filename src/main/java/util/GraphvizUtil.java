package util;

import entity.GraphNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
import com.github.javaparser.utils.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class GraphvizUtil {
    public static void createDotFile(String inputPath, String outputPath) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(new File(inputPath));

        DotPrinter printer = new DotPrinter(true);
        try (FileWriter fileWriter = new FileWriter(outputPath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
//            printWriter.print(printer.output(cu));

            cu.getTypes().forEach(type ->
                    type.getMethods().forEach(method -> {
                            method.getBody().ifPresent(m -> printWriter.print(printer.output(m)));
                        }

                    )
            );

        }
    }

    /**
     * Create a graph, different edge type has different color
     * @param graphNodeList all the nodes in the graph
     * @param edgeMap all the edges classified by c(control only), d(data only), cd(control and data only)
     * @throws IOException
     */
    public static void createGraphWithColor(String filePath, List<GraphNode> graphNodeList, Map<String, List<Pair<String, String>>> edgeMap) throws IOException {
        PrintWriter out = new PrintWriter(filePath);

        StringBuilder finalResult = new StringBuilder();

        String header = "digraph callGraph {\n\tnode [shape=rectangle]\n";

        StringBuilder nodesInResult = new StringBuilder();
        for(GraphNode graphNode : graphNodeList){
            nodesInResult.append("\t")
                    .append(graphNode.getId())
                    .append("  [label=\"")
                    .append(graphNode.getNodeName().replace("\"", "\\\""))
                    .append("\"]\n");
        }

        StringBuilder cEdgesInResult = new StringBuilder();
        for(Pair<String, String> edge : edgeMap.get("c")){
            cEdgesInResult.append("\t")
                    .append(edge.a)
                    .append(" -> ")
                    .append(edge.b)
                    .append("[label=\"")
                    .append("c")
                    .append("\", ")
                    .append("style=bold")
                    .append("]\n");
        }

        StringBuilder dEdgesInResult = new StringBuilder();
        for(Pair<String, String> edge : edgeMap.get("d")){
            // [color=red, style=dashed]
            dEdgesInResult.append("\t")
                    .append(edge.a)
                    .append(" -> ")
                    .append(edge.b)
                    .append("[label=\"")
                    .append("d")
                    .append("\", ")
                    .append("color=red, style=dashed")
                    .append("]\n");
        }

        StringBuilder cdEdgesInResult = new StringBuilder();
        for(Pair<String, String> edge : edgeMap.get("cd")){
            cdEdgesInResult.append("\t")
                    .append(edge.a)
                    .append(" -> ")
                    .append(edge.b)
                    .append("[label=\"")
                    .append("cd")
                    .append("\", ")
                    .append("color=blue")
                    .append("]\n");
        }

        finalResult.append(header).append(nodesInResult).append(cEdgesInResult).append(dEdgesInResult).append(cdEdgesInResult).append("}");

//        finalResult.append(header).append(nodesInResult).append(edgesInResult).append("}");


        out.print(finalResult);
        out.flush();
        out.close();
    }

    /**
     * Create a graph
     * @param graphNodeList all the nodes in the graph
     * @param edgeMap all the edges classified by c(control only), d(data only), cd(control and data only)
     * @throws IOException
     */
    @Deprecated
    public static void createGraph(String filePath, List<GraphNode> graphNodeList, Map<String, List<Pair<String, String>>> edgeMap) throws IOException {
        PrintWriter out = new PrintWriter(filePath);

        StringBuilder finalResult = new StringBuilder();

        String header = "digraph callGraph {\n\tnode [shape=rectangle]\n";

        StringBuilder nodesInResult = new StringBuilder();
        graphNodeList.forEach(graphNode ->
                nodesInResult.append("\t")
                        .append(graphNode.getId())
                        .append("  [label=\"")
                        .append(graphNode.getNodeName().replace("\"", "\\\""))
                        .append("\"]\n")
        );


        StringBuilder edgesInResult = new StringBuilder();

        edgeMap.keySet().forEach(
                type -> edgeMap.get(type).forEach(
                        edge -> edgesInResult.append("\t")
                                .append(edge.a)
                                .append(" -> ")
                                .append(edge.b)
                                .append("[label=\"")
                                .append(type)
                                .append("\"]\n")
                )

        );

        finalResult.append(header)
                .append(nodesInResult)
                .append(edgesInResult)
                .append("}");

        out.print(finalResult);
        out.flush();
        out.close();
    }

}
