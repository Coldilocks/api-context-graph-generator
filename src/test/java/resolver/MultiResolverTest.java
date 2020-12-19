package resolver;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author coldilock
 */
public class MultiResolverTest {

    private static final String filePath4JdkMethod = "src/test/resources/testcase/Task1.java";

    private static final String filePath4CustomMethod = "src/test/resources/testcase/Task2.java";

    private static String jarFile = "/Users/coldilock/Downloads/javaparser-core-3.16.1.jar";

    /**
     * ReflectionTypeSolver Usage
     *
     * Only available for JDK class type,
     * get qualified method signature for method call,
     * get qualified name for variable declaration expression
     *
     * @throws FileNotFoundException
     */
    public static void getQualifiedJdkMethodSignatureAndVarType(String filePath) throws FileNotFoundException {
        TypeSolver typeSolver = new ReflectionTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        cu.findAll(MethodCallExpr.class).forEach(
                mce -> System.out.println(mce.resolve().getQualifiedSignature())
        );

        cu.findAll(VariableDeclarationExpr.class).forEach(
                vde -> System.out.println(vde.calculateResolvedType().describe())
        );
    }

    /**
     * CombinedTypeSolver & ReflectionTypeSolver Usage
     *
     * Only available for JDK class type,
     * get qualified method signature(including Return type & throws & method modifier) for method call,
     * get qualified name for variable declaration expression
     *
     * @throws FileNotFoundException
     */
    public static void getQualifiedJdkMethodSignatureReturnTypeAndVarType(String filePath) throws FileNotFoundException {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        // MethodCallExpr mce = cu.findFirst(MethodCallExpr.class).get();
        // System.out.println(mce.resolve().toString());

        // get all class type of method call expression
        cu.findAll(MethodCallExpr.class).forEach(methodCallExpr -> System.out.println(methodCallExpr.resolve().toString()));

        System.out.println();
        // get all class type of variable declaration
        cu.findAll(VariableDeclarationExpr.class).forEach(variableDeclarationExpr -> System.out.println(variableDeclarationExpr.calculateResolvedType().toString()));
    }

    /**
     * JarTypeSolver Usage
     *
     * Available for user defined class type,
     * get qualified method signature of user-defined method call using JarTypeSolver
     * @throws IOException
     */
    public static void getQualifiedMethodSignatureByJarTypeSolver(String filePath) throws IOException {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
//        typeSolver.add(new JarTypeSolver(jarFile));

        typeSolver.add(JarTypeSolver.getJarTypeSolver(jarFile));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
        // get all class type of method call expression
//        cu.findAll(MethodCallExpr.class).forEach(methodCallExpr ->
//                System.out.println(methodCallExpr.resolve().toString() + "\n")
//        );

//        JavassistClassDeclaration classDecl = (JavassistClassDeclaration)typeSolver.solveType("CompilationUnit");
//        JavassistMethodDeclaration method = findMethodWithName(classDecl, "cu");
//        try {
//            method.getReturnType();
//            throw new RuntimeException("should throw UnsolvedSymbolException");
//        } catch (Exception e) {
//            assert e instanceof UnsolvedSymbolException;
//        }

        cu.findAll(MethodCallExpr.class).forEach(mce -> {
//            ResolvedType resolvedType = mce.calculateResolvedType();
            String resolvedType = mce.resolve().getQualifiedSignature();
            System.out.println("[" + mce.toString() + "]" + " is a: " + resolvedType);
        });

    }

//    private static JavassistMethodDeclaration findMethodWithName(JavassistClassDeclaration classDecl, String name) {
//        return classDecl.getDeclaredMethods().stream().filter(methodDecl -> methodDecl.getName().equals(name))
//                .map(m -> (JavassistMethodDeclaration)m).findAny().get();
//    }

    public static void main(String[] args) throws Exception {

        /*
         * Get qualified JDK method signature for method call,
         * and get qualified class name for variable declaration
         */
        getQualifiedJdkMethodSignatureAndVarType(filePath4JdkMethod);

         System.out.println(System.lineSeparator());

        /*
         * Get qualified JDK method signature,return type & throws type for method call,
         * and get qualified class name for variable declaration
         */
        getQualifiedJdkMethodSignatureReturnTypeAndVarType(filePath4JdkMethod);

        System.out.println(System.lineSeparator());

        /*
         * Get qualified method signature of user defined method call using JarTypeSolver
         */
        getQualifiedMethodSignatureByJarTypeSolver(filePath4CustomMethod);





    }

}
