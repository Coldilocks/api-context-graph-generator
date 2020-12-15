package codeanalysis.constructor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import visitors.MethodVisitorBackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class GraphConstructor {

    /**
     * create graph for every method in the file
     * @param filePath
     */
    public void createGraph(String filePath) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    List<String> nodes = new ArrayList<>();
                    MethodVisitorBackup methodVisitorBackup = new MethodVisitorBackup();
                    method.accept(methodVisitorBackup, nodes);
                    System.out.println(nodes);
                })
        );

    }

    /**
     * create graph for one method
     */
    public void createGraphForMethod(){

    }
}
