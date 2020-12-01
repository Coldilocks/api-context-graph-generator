import codeanalysis.constructor.GraphConstructor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.checkerframework.checker.units.qual.K;
import util.DataConfig;
import visitors.ImportVisitor;

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


//    public static void main(String[] args) {
//        if(args.length == 0){
//            System.out.println("please specify the path of the config file");
//            System.exit(0);
//        } else if(!new File(args[0]).exists()){
//            System.out.printf("%s doesn't exist", args[0]);
//            System.exit(0);
//        } else {
//            DataConfig.loadConfig(args[0]);
//        }
//
//        String filePath = DataConfig.TEST_INPUT_JAVA_FILE;
//
//        GraphConstructor graphConstructor = new GraphConstructor();
//        graphConstructor.createGraph(filePath);
//
//    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/Method.java";
        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        Map<String, List<String>> importNames = new HashMap<>();
        VoidVisitor<Map<String, List<String>>> importVisitor = new ImportVisitor();
        importVisitor.visit(cu, importNames);

        for(Map.Entry<String, List<String>> entry : importNames.entrySet()){
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }


    }






}
