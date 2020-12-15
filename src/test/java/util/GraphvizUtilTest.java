package utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author coldilock
 */
public class GraphvizUtilTest {

    public static void createDotFile(String inputPath, String outputPath) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(new File(inputPath));

        DotPrinter printer = new DotPrinter(true);
        try (FileWriter fileWriter = new FileWriter(outputPath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
//            printWriter.print(printer.output(cu));
            printWriter.print(printer.output(cu.getChildNodes().get(6).getChildNodes().get(2)));

        }
    }

    public static void main(String[] args) throws IOException {
        String inputJavaFilePath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/Method.java";
        String outputDotFilePath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/ast3.dot";
        createDotFile(inputJavaFilePath, outputDotFilePath);
    }

}
