package util;

import codeanalysis.representation.Graph;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
import visitors.MethodVisitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
}
