package util;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
import com.github.javaparser.utils.Pair;
import visitors.MethodVisitor;

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
     * Create a graph
     * @param graphNodeList all the nodes in the graph
     * @param edgeMap all the edges classified by c(control only), d(data only), cd(control and data only)
     * @throws IOException
     */
    public static void createGraph(String filePath, List<GraphNode> graphNodeList, Map<String, List<Pair<String, String>>> edgeMap) throws IOException {
        PrintWriter out = new PrintWriter(filePath);

        StringBuilder result = new StringBuilder();

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
                    .append("\"]\n");
        }

        StringBuilder dEdgesInResult = new StringBuilder();
        for(Pair<String, String> edge : edgeMap.get("d")){
            dEdgesInResult.append("\t")
                    .append(edge.a)
                    .append(" -> ")
                    .append(edge.b)
                    .append("[label=\"")
                    .append("d")
                    .append("\"]\n");
        }

        StringBuilder cdEdgesInResult = new StringBuilder();
        for(Pair<String, String> edge : edgeMap.get("cd")){
            cdEdgesInResult.append("\t")
                    .append(edge.a)
                    .append(" -> ")
                    .append(edge.b)
                    .append("[label=\"")
                    .append("cd")
                    .append("\"]\n");
        }

        result.append(header).append(nodesInResult).append(cEdgesInResult).append(dEdgesInResult).append(cdEdgesInResult).append("}");

        out.print(result);
        out.flush();
        out.close();
    }

}
