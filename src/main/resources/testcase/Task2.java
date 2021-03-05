import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import visitor.visitors1.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {
    /**
     * get import list with or without asterisk
     * @throws FileNotFoundException
     */
    public static void testForImport(String filePath) throws FileNotFoundException {

        CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

        Map<String, List<String>> importNames = new HashMap<>();
        VoidVisitor<Map<String, List<String>>> importVisitor = new ImportVisitor();
        importVisitor.visit(cu, importNames);

        for(Map.Entry<String, List<String>> entry : importNames.entrySet()){
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }

    }
}
