package codeanalysis.process;

import codeanalysis.representation.Graph;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

/**
 * @author coldilock
 */
public class GraphCreator {
    /**
     * convert variable declaration Expression to graph node
     * @param vde
     * @return
     */
    public Graph convert(VariableDeclarationExpr vde){
        return null;
    }
    /**
     * convert assign expression to graph node
     * @param ae
     * @return
     */
    public Graph convert(AssignExpr ae){
        return null;
    }
}
