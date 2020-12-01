package resolver;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;

/**
 * @author coldilock
 */
public class ResolveMethodCalls {

    private static final String FILE_PATH = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/Test2.java";

    public static void main(String[] args) throws Exception {

        TypeSolver typeSolver = new ReflectionTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        cu.findAll(MethodCallExpr.class).forEach(
                mce -> System.out.println(mce.resolve().getQualifiedSignature())
        );

    }

}
