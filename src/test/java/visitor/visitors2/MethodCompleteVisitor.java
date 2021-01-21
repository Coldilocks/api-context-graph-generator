package visitor.visitors2;

import entity.Graph;
import entity.GraphNode;
//import codeanalysis.representation.GraphNodeHelper;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MethodCompleteVisitor extends CustomVoidVisitor<Graph> {

//    private GraphNodeHelper graphNodeHelper = new GraphNodeHelper();

    private StringBuilder nodeName;

    public List<String> nodeNameList = new ArrayList<>();

    private boolean checkNodeName(){
        return !nodeName.toString().isEmpty() && !nodeName.toString().startsWith(".");
    }

    @Override
    public void visit(AssertStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(BlockStmt n, Graph graph) {
        System.out.println("[BLOCK]");
        super.visit(n, graph);

//        System.out.println();
//        nodeNameList.forEach(System.out::println);
    }

    @Override
    public void visit(BreakStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ContinueStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(DoStmt n, Graph graph) {
//        System.out.println("[DO WHILE] " + n.toString());
        System.out.println("[DO WHILE]");
        GraphNode graphNode = new GraphNode();
        graphNode.setNodeName("doWhile");
        graph.addNode(graphNode);
        super.visit(n, graph);
    }

    @Override
    public void visit(EmptyStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ExpressionStmt n, Graph graph) {
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

//        graphNodeHelper.setCurrentStmt(n);

        super.visit(n, graph);

    }

    @Override
    public void visit(ForEachStmt n, Graph graph) {
//        System.out.println("[FOR EACH] " + n.toString());
        System.out.println("[FOR EACH]");
        super.visit(n, graph);
    }

    @Override
    public void visit(ForStmt n, Graph graph) {
//        System.out.println("[FOR] " + n.toString());
        System.out.println("[FOR]");
        super.visit(n, graph);
    }

    @Override
    public void visit(IfStmt n, Graph graph) {
//        System.out.println("[IF] " + n.toString());
        System.out.println("[IF]");
        super.visit(n, graph);
    }

    @Override
    public void visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ReturnStmt n, Graph graph) {
        System.out.println("[RETURN] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(SwitchStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(SynchronizedStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ThrowStmt n, Graph graph) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(TryStmt n, Graph graph) {
//        System.out.println("[TRY] " + n.toString());
        System.out.println("[TRY]");
        super.visit(n, graph);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(WhileStmt n, Graph graph) {
//        System.out.println("[WHILE] " + n.toString());
        System.out.println("[WHILE]");
        super.visit(n, graph);
    }

    @Override
    public void visit(UnparsableStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(YieldStmt n, Graph graph) {
        super.visit(n, graph);
    }

    @Override
    public void visit(ArrayAccessExpr n, Graph graph) {
        System.out.println("    <ArrayAccessExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(ArrayCreationExpr n, Graph graph) {
        System.out.println("    <ArrayCreationExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(ArrayInitializerExpr n, Graph graph) {
        System.out.println("    <ArrayInitializerExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(AssignExpr n, Graph graph) {
        nodeName = new StringBuilder();

        System.out.println("    <AssignExpr> "+ n.toString());
        // System.out.println("        ! " + n.calculateResolvedType().describe());

        nodeName.append(n.calculateResolvedType().describe());

        System.out.println("            <<" + nodeName.toString() + "...");
        super.visit(n, graph);
        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName())
            nodeNameList.add(nodeName.toString());
    }

    @Override
    public void visit(BinaryExpr n, Graph graph) {
        System.out.println("    <BinaryExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(BooleanLiteralExpr n, Graph graph) {
        System.out.println("    <BooleanLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    @Override
    public void visit(CastExpr n, Graph graph) {
        System.out.println("    <CastExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(CharLiteralExpr n, Graph graph) {
        System.out.println("    <CharLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    @Override
    public void visit(ClassExpr n, Graph graph) {
        System.out.println("    <ClassExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(ConditionalExpr n, Graph graph) {
        System.out.println("    <ConditionalExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(DoubleLiteralExpr n, Graph graph) {
        System.out.println("    <DoubleLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    @Override
    public void visit(EnclosedExpr n, Graph graph) {
        System.out.println("    <EnclosedExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(FieldAccessExpr n, Graph graph) {
        System.out.println("    <FieldAccessExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(InstanceOfExpr n, Graph graph) {
        System.out.println("    <InstanceOfExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Graph graph) {
        System.out.println("    <IntegerLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    @Override
    public void visit(LongLiteralExpr n, Graph graph) {
        System.out.println("    <LongLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    /**
     * parse the annotation, such as @Override, @Controller...
     * @param n
     * @param graph
     */
//    @Override
//    public void visit(MarkerAnnotationExpr n, Graph graph) {
//        System.out.println("    <MarkerAnnotationExpr> "+ n.toString());
//        super.visit(n, graph);
//    }

    @Override
    public void visit(MethodCallExpr n, Graph graph) {
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

        // System.out.println(nodeName);
        if(checkNodeName())
            nodeNameList.add(nodeName.toString());

        nodeName = new StringBuilder();


        /*
         * obj.method1().method2()
         * output: method2() -> method1()
         *
         * obj.method1(method2())
         * output: method1() -> method2()
         */
        // super.visit(n, graph);
    }

    @Override
    public void visit(NameExpr n, Graph graph) {
//        System.out.println("    <NameExpr> "+ n.toString());
//        System.out.println("        @ " + n.calculateResolvedType().describe());
        super.visit(n, graph);
    }

    @Override
    public void visit(NormalAnnotationExpr n, Graph graph) {
        System.out.println("    <NormalAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(NullLiteralExpr n, Graph graph) {
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
    }

    @Override
    public void visit(ObjectCreationExpr n, Graph graph) {
        // store the type of object
        String savedClazzName = nodeName.toString();

        // examine the method call inside the constructor at first
        super.visit(n, graph);

        System.out.println("    <ObjectCreationExpr> "+ n.toString());
        // System.out.println("        % " + n.calculateResolvedType().describe());

        // restore the type of object
        nodeName = new StringBuilder();
        nodeName.append(savedClazzName);

        try{
            // System.out.println("        % " + n.resolve().getQualifiedSignature());
            String objCreationName = n.resolve().getQualifiedSignature();
            nodeName.append(".new").append("(").append(objCreationName, objCreationName.indexOf("(") + 1, objCreationName.indexOf(")") + 1 );
            if(checkNodeName())
                nodeNameList.add(nodeName.toString());
            nodeName = new StringBuilder();
        } catch (UnsolvedSymbolException e){
            // System.out.println("        % can't resolve the type of " + n.toString());
            if(checkNodeName()){
                nodeNameList.add("UnresolvableType");
            }
            nodeName = new StringBuilder();
        }
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Graph graph) {
        System.out.println("    <SingleMemberAnnotationExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(StringLiteralExpr n, Graph graph) {
        System.out.println("    <StringLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        nodeName.append(".Constant");
        // System.out.println("            (" + nodeName.toString() + ")");
        super.visit(n, graph);
    }

    @Override
    public void visit(SuperExpr n, Graph graph) {
        System.out.println("    <SuperExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(UnaryExpr n, Graph graph) {
        System.out.println("    <UnaryExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(ThisExpr n, Graph graph) {
        System.out.println("    <ThisExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Graph graph) {
        // System.out.println("    <VariableDeclarationExpr> "+ n.toString());
        // System.out.println("        $ " + n.calculateResolvedType().describe());

        super.visit(n, graph);
    }

    @Override
    public void visit(LambdaExpr n, Graph graph) {
        System.out.println("    <LambdaExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(MethodReferenceExpr n, Graph graph) {
        System.out.println("    <MethodReferenceExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(TypeExpr n, Graph graph) {
        System.out.println("    <TypeExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(SwitchExpr n, Graph graph) {
        System.out.println("    <SwitchExpr> "+ n.toString());
        super.visit(n, graph);
    }

    @Override
    public void visit(TextBlockLiteralExpr n, Graph graph) {
        System.out.println("    <TextBlockLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        super.visit(n, graph);
    }

    @Override
    public void visit(VariableDeclarator n, Graph graph) {
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
        super.visit(n, graph);
        System.out.println("            ..." + nodeName.toString() + ">>");

        if(checkNodeName())
            nodeNameList.add(nodeName.toString());
    }


    public void visit2(VariableDeclarator n, Graph graph) {
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
    }
}
