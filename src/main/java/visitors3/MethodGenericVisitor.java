package visitors3;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import codeanalysis.representation.GraphNodeHelper;
import codeanalysis.representation.node.AssignmentNode;
import codeanalysis.representation.node.MethodCallNode;
import codeanalysis.representation.node.controlunitnode.BodyNode;
import codeanalysis.representation.node.controlunitnode.DoWhileNode;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MethodGenericVisitor extends CustomGenericVisitor<GraphNode, Graph> {

    private GraphNodeHelper graphNodeHelper = new GraphNodeHelper();

    private StringBuilder nodeName;

    private List<String> nodeNameList = new ArrayList<>();

    private boolean checkNodeName(){
        return !nodeName.toString().isEmpty() && !nodeName.toString().startsWith(".");
    }
    

    @Override
    public GraphNode visit(AssertStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(BlockStmt n, Graph graph) {
        System.out.println("[BLOCK]");

        GraphNode graphNode = new BodyNode();
        graphNode.setNodeName("Body");


        GraphNode childNode =super.visit(n, graph);
        graphNode.addChildNode(childNode);

        System.out.println();
        nodeNameList.forEach(System.out::println);


        return graphNode;
    }

    @Override
    public GraphNode visit(BreakStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ContinueStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(DoStmt n, Graph graph) {
//        System.out.println("[DO WHILE] " + n.toString());
        System.out.println("[DO WHILE]");
        GraphNode graphNode = new DoWhileNode();
        graphNode.setNodeName("doWhile");
        graph.addNode(graphNode);

        GraphNode testNode = super.visit(n, graph);
        graphNode.addChildNode(testNode);

        return graphNode;
    }

    @Override
    public GraphNode visit(EmptyStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ExplicitConstructorInvocationStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ExpressionStmt n, Graph graph) {
        /*
         * todo:
         *  Expression like "File file = new File(path);" can be parsed
         *  and the qualified type of File can be retrieved.
         *  However, the type of some method invocations like "file.mkdir();"
         *  will not be successfully retrieved.
         *  Cause what we get by code "n.getExpression().calculateResolvedType().describe()"
         *  for "file.mkdir();" is the return type of method mkdir().
         *
         */
        System.out.println("\n[EXPRESSION] " + n.getExpression().toString());

        graphNodeHelper.setCurrentStmt(n);

        super.visit(n, graph);

        return null;
    }

    @Override
    public GraphNode visit(ForEachStmt n, Graph graph) {
//        System.out.println("[FOR EACH] " + n.toString());
        System.out.println("[FOR EACH]");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ForStmt n, Graph graph) {
//        System.out.println("[FOR] " + n.toString());
        System.out.println("[FOR]");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(IfStmt n, Graph graph) {
//        System.out.println("[IF] " + n.toString());
        System.out.println("[IF]");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ReturnStmt n, Graph graph) {
        System.out.println("[RETURN] " + n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(SwitchStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(SynchronizedStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ThrowStmt n, Graph graph) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(TryStmt n, Graph graph) {
//        System.out.println("[TRY] " + n.toString());
        System.out.println("[TRY]");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(WhileStmt n, Graph graph) {
//        System.out.println("[WHILE] " + n.toString());
        System.out.println("[WHILE]");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(UnparsableStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(YieldStmt n, Graph graph) {
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ArrayAccessExpr n, Graph graph) {
        System.out.println("    <ArrayAccessExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ArrayCreationExpr n, Graph graph) {
        System.out.println("    <ArrayCreationExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ArrayInitializerExpr n, Graph graph) {
        System.out.println("    <ArrayInitializerExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(AssignExpr n, Graph graph) {
        nodeName = new StringBuilder();

        System.out.println("    <AssignExpr> "+ n.toString());
        // System.out.println("        ! " + n.calculateResolvedType().describe());

        nodeName.append(n.calculateResolvedType().describe());

        System.out.println("            <<" + nodeName.toString() + "...");

        super.visit(n, graph);

        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName()){
            nodeNameList.add(nodeName.toString());

            GraphNode graphNode = new AssignmentNode();
            graphNode.setNodeName(nodeName.toString());
            return graphNode;

        }

        return null;
    }

    @Override
    public GraphNode visit(BinaryExpr n, Graph graph) {
        System.out.println("    <BinaryExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(BooleanLiteralExpr n, Graph graph) {
        System.out.println("    <BooleanLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(CastExpr n, Graph graph) {
        System.out.println("    <CastExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(CharLiteralExpr n, Graph graph) {
        System.out.println("    <CharLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ClassExpr n, Graph graph) {
        System.out.println("    <ClassExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ConditionalExpr n, Graph graph) {
        System.out.println("    <ConditionalExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(DoubleLiteralExpr n, Graph graph) {
        System.out.println("    <DoubleLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(EnclosedExpr n, Graph graph) {
        System.out.println("    <EnclosedExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(FieldAccessExpr n, Graph graph) {
        System.out.println("    <FieldAccessExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(InstanceOfExpr n, Graph graph) {
        System.out.println("    <InstanceOfExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(IntegerLiteralExpr n, Graph graph) {
        System.out.println("    <IntegerLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(LongLiteralExpr n, Graph graph) {
        System.out.println("    <LongLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    /**
     * parse the annotation, such as @Override, @Controller...
     * @param n
     * @param graph
     */
//    @Override
//    public GraphNode visit(MarkerAnnotationExpr n, Graph graph) {
//        System.out.println("    <MarkerAnnotationExpr> "+ n.toString());
//        super.visit(n, graph);
//        return null;
//    }

    @Override
    public GraphNode visit(MethodCallExpr n, Graph graph) {
        /*
         * obj.method1().method2()
         * output: method1() -> method2()
         *
         * obj.method1(method2())
         * output: method2() -> method1()
         */

        super.visit(n, graph);

        System.out.println("    <MethodCallExpr> "+ n.toString());
//        System.out.println("        # "+n.resolve().getQualifiedSignature());

        nodeName = new StringBuilder();
        nodeName.append(n.resolve().getQualifiedSignature());

        System.out.println(nodeName);
        if(checkNodeName()){
            nodeNameList.add(nodeName.toString());

            GraphNode graphNode = new MethodCallNode();
            graphNode.setNodeName(nodeName.toString());

            nodeName = new StringBuilder();
            return graphNode;
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
        return null;
    }

    @Override
    public GraphNode visit(NameExpr n, Graph graph) {
//        System.out.println("    <NameExpr> "+ n.toString());
//        System.out.println("        @ " + n.calculateResolvedType().describe());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(NormalAnnotationExpr n, Graph graph) {
        System.out.println("    <NormalAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(NullLiteralExpr n, Graph graph) {
        System.out.println("    <NullLiteralExpr> "+ n.toString());
        // System.out.println("        $ .NULL");
//        if(n.getParentNode().isPresent())
        n.getParentNode().ifPresent(node -> {
            if(node instanceof VariableDeclarator){
                nodeName.append(".Declaration").append(".NULL");
            } else if(node instanceof AssignExpr){
                nodeName.append(".NULL");
            }
        });
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ObjectCreationExpr n, Graph graph) {
        System.out.println("    <ObjectCreationExpr> "+ n.toString());
        // System.out.println("        % " + n.calculateResolvedType().describe());
        try{
//            System.out.println("        % " + n.resolve().getQualifiedSignature());
            String objCreationName = n.resolve().getQualifiedSignature();
            nodeName.append(".new").append("(").append(objCreationName, objCreationName.indexOf("(") + 1, objCreationName.indexOf(")") + 1 );
            if(checkNodeName()){
                nodeNameList.add(nodeName.toString());

                GraphNode graphNode = new MethodCallNode();
                graphNode.setNodeName(nodeName.toString());

                nodeName = new StringBuilder();
                return graphNode;
            }

        } catch (UnsolvedSymbolException e){
            System.out.println("        % can't resolve the type of " + n.toString());
            // todo: check if this is correct
            nodeName = new StringBuilder();
        }

        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(SingleMemberAnnotationExpr n, Graph graph) {
        System.out.println("    <SingleMemberAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(StringLiteralExpr n, Graph graph) {
        System.out.println("    <StringLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(SuperExpr n, Graph graph) {
        System.out.println("    <SuperExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(UnaryExpr n, Graph graph) {
        System.out.println("    <UnaryExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(ThisExpr n, Graph graph) {
        System.out.println("    <ThisExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(VariableDeclarationExpr n, Graph graph) {
        // System.out.println("    <VariableDeclarationExpr> "+ n.toString());
        // System.out.println("        $ " + n.calculateResolvedType().describe());

//        GraphNode graphNode = new GraphNode();
//        graphNode.setNodeName(n.calculateResolvedType().describe());
////        graphNode.setJapaStatement(n);
//        graph.addNode(graphNode);

        GraphNode graphNode = super.visit(n, graph);
        return graphNode;
    }

    @Override
    public GraphNode visit(LambdaExpr n, Graph graph) {
        System.out.println("    <LambdaExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(MethodReferenceExpr n, Graph graph) {
        System.out.println("    <MethodReferenceExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(TypeExpr n, Graph graph) {
        System.out.println("    <TypeExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(SwitchExpr n, Graph graph) {
        System.out.println("    <SwitchExpr> "+ n.toString());
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(TextBlockLiteralExpr n, Graph graph) {
        System.out.println("    <TextBlockLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        super.visit(n, graph);
        return null;
    }

    @Override
    public GraphNode visit(VariableDeclarator n, Graph graph) {
        // System.out.println("    <VariableDeclarator>" + n.toString());

        nodeName = new StringBuilder();

        n.getParentNode().ifPresent(parentNode -> {
            VariableDeclarationExpr vde = (VariableDeclarationExpr) parentNode;
            System.out.println("    <VariableDeclarationExpr> "+ vde.toString());
            // System.out.println("        $ " + vde.calculateResolvedType().describe());
            nodeName.append(vde.calculateResolvedType().describe());
        });

        System.out.println("    <VariableDeclarator>");

        if(!n.getInitializer().isPresent()){
//        if(n.getInitializer().isPresent()){
            // System.out.println("        $ .Declaration");
            nodeName.append(".Declaration");
        }
//        else if(n.getInitializer().get().isLiteralExpr()){
//            if(n.getInitializer().get().isNullLiteralExpr()){
//                nodeName.append(".NULL");
//            } else {
//                nodeName.append(".Constant");
//            }
//        }
        System.out.println("            <<" + nodeName.toString() + "...");
        super.visit(n, graph);
        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName())
            nodeNameList.add(nodeName.toString());

        return null;
    }


    public GraphNode visit2(VariableDeclarator n, Graph graph) {
        // System.out.println("    <VariableDeclarator>" + n.toString());

        nodeName = new StringBuilder();

        n.getParentNode().ifPresent(parentNode -> {
            VariableDeclarationExpr vde = (VariableDeclarationExpr) parentNode;
            System.out.println("    <VariableDeclarationExpr> "+ vde.toString());
            // System.out.println("        $ " + vde.calculateResolvedType().describe());
            nodeName.append(vde.calculateResolvedType().describe());
        });

        System.out.println("    <VariableDeclarator>");

        if(!n.getInitializer().isPresent()){
            // declare a variable but don't initialize it
            nodeName.append(".Declaration");
        } else if(n.getInitializer().get().isLiteralExpr()){
            Expression tempExpression = n.getInitializer().get();
            if(tempExpression.isNullLiteralExpr()){
                nodeName.append(".NULL");
            } else if(tempExpression.isLiteralExpr()){
                nodeName.append(".Constant");
            } else if(tempExpression.isAssignExpr()){

            } else if(tempExpression.isMethodCallExpr()){

            } else if(tempExpression.isObjectCreationExpr()){

            }
        }
        System.out.println("            <<" + nodeName.toString() + "...");
        super.visit(n, graph);
        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName())
            nodeNameList.add(nodeName.toString());

        return null;
    }
}
