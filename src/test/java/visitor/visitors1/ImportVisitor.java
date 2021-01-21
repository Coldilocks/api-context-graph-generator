package visitor.visitors1;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class ImportVisitor extends VoidVisitorAdapter<Map<String, List<String>>> {
    @Override
    public void visit(ImportDeclaration id, Map<String, List<String>> collector) {
        super.visit(id, collector);

        if(id.isAsterisk()){
            collector.computeIfAbsent("import_with_asterisk", k -> new ArrayList<>()).add(id.getNameAsString());
        } else {
            collector.computeIfAbsent("import_without_asterisk", k -> new ArrayList<>()).add(id.getNameAsString());
        }

    }
}
