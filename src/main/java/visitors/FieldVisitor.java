package visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * @author coldilock
 */
public class FieldVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(FieldDeclaration fd, List<String> collector) {
        super.visit(fd, collector);
//        collector.add(fd);
    }
}
