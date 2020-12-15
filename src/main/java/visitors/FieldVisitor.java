package visitors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * @author coldilock
 */
public class FieldVisitor extends VoidVisitorAdapter<List<VariableDeclarationExpr>> {
    @Override
    public void visit(FieldDeclaration fd, List<VariableDeclarationExpr> collector) {
        super.visit(fd, collector);

        VariableDeclarationExpr expr = new VariableDeclarationExpr();


        for(VariableDeclarator vd : fd.getVariables()){
            NodeList<VariableDeclarator> vdList = new NodeList<>();
            vdList.add(vd);
            expr.setAllTypes(fd.getElementType());
            expr.setVariables(vdList);

            collector.add(expr);
        }


    }
}
