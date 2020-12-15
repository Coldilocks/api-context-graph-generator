package visitors;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class MethodVisitor extends VoidVisitorAdapter<Graph> {

    private Map<String, String> parameters = new HashMap<>();

    @Override
    public void visit(MethodDeclaration md, Graph graph) {
        /*
         * todo:获取参数
         */


        /*
         * todo:为每个statement创建节点
         */
        md.getBody().ifPresent(body -> {
            for(Statement stm : body.getStatements()){
                GraphNode node = new GraphNode();
                node.setNodeName(stm.toString());
                node.setOriginalStatement(stm.toString());
                node.setJapaStatement(stm);
                graph.addNode(node);
            }
        });

        super.visit(md, graph);
    }
}
