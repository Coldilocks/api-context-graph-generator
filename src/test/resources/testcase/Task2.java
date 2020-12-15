import codeanalysis.constructor.GraphConstructor;
import codeanalysis.representation.Graph;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import util.DataConfig;
import visitors.*;
import visitors2.MethodStmtVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class Test {

    private static String filePath = "src/test/resources/testcase/Method1-8.java";


    public static void main(String[] args) throws FileNotFoundException {
        // get import list with or without asterisk
         testForImport();
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
}
