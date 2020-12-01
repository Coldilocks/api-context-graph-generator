import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import util.DataConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author coldilock
 */
public class Test {

    public static String getContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("请指定配置文件路径");
            System.exit(0);
        }
        if(!new File(args[0]).exists()){
            System.out.printf("%s 不存在", args[0]);
            System.exit(0);
        } else {
            DataConfig.loadConfig(args[0]);
        }

        String filePath = DataConfig.TEST_INPUT_JAVA_FILE;

    }


}
