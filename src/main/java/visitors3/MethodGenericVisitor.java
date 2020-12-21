package visitors3;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import codeanalysis.representation.GraphNodeHelper;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MethodGenericVisitor extends CustomGenericListVisitor<GraphNode, Graph> {

    private GraphNodeHelper graphNodeHelper = new GraphNodeHelper();

    private StringBuilder nodeName;

    public List<String> nodeNameList = new ArrayList<>();

    private boolean checkNodeName(){
        return !nodeName.toString().isEmpty() && !nodeName.toString().startsWith(".");
    }

    @Override
    public List<GraphNode> visit(AssertStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BlockStmt n, Graph graph) {
        System.out.println("[BLOCK]");
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode currentNode = new GraphNode("Body");

        // super.visit(n, graph);
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BreakStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ContinueStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(DoStmt n, Graph graph) {
//        System.out.println("[DO WHILE] " + n.toString());
        System.out.println("[DO WHILE]");
//        GraphNode graphNode = new DoWhileNode();
//        graphNode.setNodeName("doWhile");
//        graph.addNode(graphNode);

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode currentNode = new GraphNode("doWhile");

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);
        
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(EmptyStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ExplicitConstructorInvocationStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ExpressionStmt n, Graph graph) {

        System.out.println("\n[EXPRESSION] " + n.getExpression().toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        // super.visit(n, graph);
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ForEachStmt n, Graph graph) {
        System.out.println("[FOR EACH]");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ForStmt n, Graph graph) {
//        System.out.println("[FOR] " + n.toString());
        System.out.println("[FOR]");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(IfStmt n, Graph graph) {
        System.out.println("[IF]");

        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode currentNode = new GraphNode("If");

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);

        return graphNodes;
    }



    @Override
    public List<GraphNode> visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ReturnStmt n, Graph graph) {
        System.out.println("[RETURN] " + n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        graphNodes.add(new GraphNode("Return"));

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SwitchStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SynchronizedStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ThrowStmt n, Graph graph) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TryStmt n, Graph graph) {
//        System.out.println("[TRY] " + n.toString());
        System.out.println("[TRY]");
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode currentNode = new GraphNode("Try");

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(WhileStmt n, Graph graph) {
        System.out.println("[WHILE]");
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode currentNode = new GraphNode("While");

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(UnparsableStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(YieldStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ArrayAccessExpr n, Graph graph) {
        System.out.println("    <ArrayAccessExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ArrayCreationExpr n, Graph graph) {
        System.out.println("    <ArrayCreationExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ArrayInitializerExpr n, Graph graph) {
        System.out.println("    <ArrayInitializerExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(AssignExpr n, Graph graph) {
        nodeName = new StringBuilder();
        List<GraphNode> graphNodes = new ArrayList<>();

        System.out.println("    <AssignExpr> "+ n.toString());
        // System.out.println("        ! " + n.calculateResolvedType().describe());

        nodeName.append(n.calculateResolvedType().describe());

        System.out.println("            <<" + nodeName.toString() + "...");
        // graphNodes.addAll(super.visit(n, graph));
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName()){
            nodeNameList.add(nodeName.toString());
            graphNodes.add(new GraphNode(nodeName.toString()));
        }
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BinaryExpr n, Graph graph) {
        System.out.println("    <BinaryExpr> "+ n.toString());
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childeNodes = super.visit(n, graph);
        if(childeNodes != null)
            graphNodes.addAll(childeNodes);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BooleanLiteralExpr n, Graph graph) {
        System.out.println("    <BooleanLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CastExpr n, Graph graph) {
        System.out.println("    <CastExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CharLiteralExpr n, Graph graph) {
        System.out.println("    <CharLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ClassExpr n, Graph graph) {
        System.out.println("    <ClassExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ConditionalExpr n, Graph graph) {
        System.out.println("    <ConditionalExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(DoubleLiteralExpr n, Graph graph) {
        System.out.println("    <DoubleLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(EnclosedExpr n, Graph graph) {
        System.out.println("    <EnclosedExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(FieldAccessExpr n, Graph graph) {
        System.out.println("    <FieldAccessExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(InstanceOfExpr n, Graph graph) {
        System.out.println("    <InstanceOfExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(IntegerLiteralExpr n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();
        System.out.println("    <IntegerLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LongLiteralExpr n, Graph graph) {
        System.out.println("    <LongLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
        
    }

    /**
     * parse the annotation, such as @Override, @Controller...
     * @param n
     * @param graph
     */
//    @Override
//    public List<GraphNode> visit(MarkerAnnotationExpr n, Graph graph) {
//        System.out.println("    <MarkerAnnotationExpr> "+ n.toString());
//        super.visit(n, graph);
//    }

    @Override
    public List<GraphNode> visit(MethodCallExpr n, Graph graph) {

        List<GraphNode> graphNodes = new ArrayList<>();
        /*
         * obj.method1().method2()
         * output: method1() -> method2()
         *
         * obj.method1(method2())
         * output: method2() -> method1()
         */

        // todo: check if this is correct
        // examine the method call inside the parameter list of the current method call expression at first
        // graphNodes.addAll(super.visit(n, graph));
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        System.out.println("    <MethodCallExpr> "+ n.toString());
        // System.out.println("        # "+n.resolve().getQualifiedSignature());

        nodeName = new StringBuilder();
        nodeName.append(n.resolve().getQualifiedSignature());

        // System.out.println(nodeName);
        if(checkNodeName()){
            nodeNameList.add(nodeName.toString());
            graphNodes.add(new GraphNode(nodeName.toString()));
        }

        nodeName = new StringBuilder();

        /*
         * obj.method1().method2()
         * output: method2() -> method1()
         *
         * obj.method1(method2())
         * output: method1() -> method2()
         */
        // super.visit(n, graph);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NameExpr n, Graph graph) {
        // System.out.println("    <NameExpr> "+ n.toString());
        // System.out.println("        @ " + n.calculateResolvedType().describe());
        List<GraphNode> graphNodes = new ArrayList<>();
        // super.visit(n, graph);
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NormalAnnotationExpr n, Graph graph) {
        System.out.println("    <NormalAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NullLiteralExpr n, Graph graph) {
        System.out.println("    <NullLiteralExpr> "+ n.toString());
        // System.out.println("        $ .NULL");
        // if(n.getParentNode().isPresent())
        n.getParentNode().ifPresent(node -> {
            if(node instanceof VariableDeclarator){
                nodeName.append(".Declaration").append(".NULL");
            } else if(node instanceof AssignExpr){
                nodeName.append(".NULL");
            }
        });
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ObjectCreationExpr n, Graph graph) {
        // store the type of object
        String savedClazzName = nodeName.toString();

        List<GraphNode> graphNodes = new ArrayList<>();

        // examine the method call inside the constructor at first
        // todo: check if this is correct
        // graphNodes.addAll(super.visit(n, graph));
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        System.out.println("    <ObjectCreationExpr> "+ n.toString());

        // restore the type of object
        nodeName = new StringBuilder();
        nodeName.append(savedClazzName);

        // System.out.println("        % " + n.calculateResolvedType().describe());
        try{
            // System.out.println("        % " + n.resolve().getQualifiedSignature());
            String objCreationName = n.resolve().getQualifiedSignature();
            nodeName.append(".new").append("(").append(objCreationName, objCreationName.indexOf("(") + 1, objCreationName.indexOf(")") + 1 );
            if(checkNodeName()){
                nodeNameList.add(nodeName.toString());
                graphNodes.add(new GraphNode(nodeName.toString()));
            }
            nodeName = new StringBuilder();
        } catch (UnsolvedSymbolException e){
            // todo: solve this problem
            // System.out.println("        % can't resolve the type of " + n.toString());
            if(checkNodeName()){
                nodeNameList.add("UnresolvableType");
                graphNodes.add(new GraphNode("UnresolvableType"));
            }
            nodeName = new StringBuilder();
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SingleMemberAnnotationExpr n, Graph graph) {
        System.out.println("    <SingleMemberAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(StringLiteralExpr n, Graph graph) {
        System.out.println("    <StringLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SuperExpr n, Graph graph) {
        System.out.println("    <SuperExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(UnaryExpr n, Graph graph) {
        System.out.println("    <UnaryExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ThisExpr n, Graph graph) {
        System.out.println("    <ThisExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(VariableDeclarationExpr n, Graph graph) {
        // System.out.println("    <VariableDeclarationExpr> "+ n.toString());
        // System.out.println("        $ " + n.calculateResolvedType().describe());
        List<GraphNode> graphNodes = new ArrayList<>();
        // graphNodes.addAll(super.visit(n, graph));
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LambdaExpr n, Graph graph) {
        System.out.println("    <LambdaExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(MethodReferenceExpr n, Graph graph) {
        System.out.println("    <MethodReferenceExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TypeExpr n, Graph graph) {
        System.out.println("    <TypeExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SwitchExpr n, Graph graph) {
        System.out.println("    <SwitchExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TextBlockLiteralExpr n, Graph graph) {
        System.out.println("    <TextBlockLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(VariableDeclarator n, Graph graph) {
        // System.out.println("    <VariableDeclarator>" + n.toString());

        nodeName = new StringBuilder();
        List<GraphNode> graphNodes = new ArrayList<>();

        n.getParentNode().ifPresent(parentNode -> {
            VariableDeclarationExpr vde = (VariableDeclarationExpr) parentNode;
            System.out.println("    <VariableDeclarationExpr> "+ vde.toString());
            // System.out.println("        $ " + vde.calculateResolvedType().describe());
            nodeName.append(vde.calculateResolvedType().describe());
        });

        System.out.println("    <VariableDeclarator>");

        if(!n.getInitializer().isPresent()){
            // if(n.getInitializer().isPresent()){
            // System.out.println("        $ .Declaration");
            nodeName.append(".Declaration");
        }
        // else if(n.getInitializer().get().isLiteralExpr()){
        // if(n.getInitializer().get().isNullLiteralExpr()){
        // nodeName.append(".NULL");
        // } else {
        // nodeName.append(".Constant");
        // }
        // }
        System.out.println("            <<" + nodeName.toString() + "...");

        // graphNodes.addAll(super.visit(n, graph));
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName()){
            nodeNameList.add(nodeName.toString());
            graphNodes.add(new GraphNode(nodeName.toString()));
        }
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CatchClause n, Graph graph) {
        System.out.println("[CATCH]");

        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode currentNode = new GraphNode("Catch");

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            for(GraphNode tempNode : childNodes){
                currentNode.addChildNode(tempNode);
            }
        }

        graphNodes.add(currentNode);

        return graphNodes;
    }


}
