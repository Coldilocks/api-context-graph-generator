package visitorsx;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MethodVisitorX extends GenericVisitorAdapterX<GraphNode, Graph> {

    public List<String> nodeNameList = new ArrayList<>();

    private boolean checkNodeName(String str){
        return !str.isEmpty() && !str.startsWith(".");
    }

    @Override
    public List<GraphNode> visit(MethodDeclaration n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();

        n.getBody().ifPresent(blockStmt -> {
            blockStmt.getStatements().forEach(statement -> {
                List<GraphNode> childNodes = statement.accept(this, graph);
                graphNodes.addAll(childNodes);
            });
        });

        GraphNode rootNode = new GraphNode("Root");
        rootNode = graph.linkGraph(rootNode, graphNodes);

        List<GraphNode> graphResult = new ArrayList<>();
        graphResult.add(rootNode);
        return graphResult;

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

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

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

    /**
     * do while statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(DoStmt n, Graph graph) {
        System.out.println("[DO WHILE]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode doWhileNode = new GraphNode("doWhile");

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition");
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, graph);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            doWhileNode.addChildNode(conditionNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body");
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, graph);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            doWhileNode.addChildNode(bodyNode);
        }

        graphNodes.add(doWhileNode);

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

    /**
     * expression statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ExpressionStmt n, Graph graph) {

        System.out.println("\n[EXPRESSION] " + n.getExpression().toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);

        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));

        return graphNodes;
    }

    /**
     * enchaned for statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ForEachStmt n, Graph graph) {
        System.out.println("[FOR EACH]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forEachNode = new GraphNode("ForEach");

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization");
        List<GraphNode> initializationChildNodes = n.getVariable().accept(this, graph);
        if(initializationChildNodes != null){
            initializationNode = graph.linkGraph(initializationNode, initializationChildNodes);
            forEachNode.addChildNode(initializationNode);
        }

        // examine the iterable expression and get iterable node
        GraphNode iterableNode = new GraphNode("Iterable");
        List<GraphNode> iterableChildNodes = n.getIterable().accept(this, graph);
        if(iterableChildNodes != null){
            iterableNode = graph.linkGraph(iterableNode, iterableChildNodes);
            forEachNode.addChildNode(iterableNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body");
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, graph);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            forEachNode.addChildNode(bodyNode);
        }

        graphNodes.add(forEachNode);

        return graphNodes;
    }

    /**
     * for statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ForStmt n, Graph graph) {
        System.out.println("[FOR]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forNode = new GraphNode("For");

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization");
        List<GraphNode> initializationChildNodes = n.getInitialization().accept(this, graph);
        if(initializationChildNodes != null){
            initializationNode = graph.linkGraph(initializationNode, initializationChildNodes);
            forNode.addChildNode(initializationNode);
        }

        // examine the compare expression and get compare node
        GraphNode compareNode = new GraphNode("Compare");
        List<GraphNode> compareChildNodes = null;
        if(n.getCompare().isPresent()){
            compareChildNodes = n.getCompare().get().accept(this, graph);
        }
        if(compareChildNodes != null){
            compareNode = graph.linkGraph(compareNode, compareChildNodes);
            forNode.addChildNode(compareNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body");
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, graph);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            forNode.addChildNode(bodyNode);
        }

        // examine the update expression and get update node
        GraphNode updateNode = new GraphNode("Update");
        List<GraphNode> updateChildNodes = n.getUpdate().accept(this, graph);
        if(updateChildNodes != null){
            updateNode = graph.linkGraph(updateNode, updateChildNodes);
            forNode.addChildNode(updateNode);
        }

        graphNodes.add(forNode);

        return graphNodes;
    }

    /**
     * if statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(IfStmt n, Graph graph) {
        System.out.println("[IF]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode ifNode = new GraphNode("If");

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition");
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, graph);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            ifNode.addChildNode(conditionNode);
        }

        // examine the then stmt and get then node
        GraphNode thenNode = new GraphNode("Then");
        List<GraphNode> thenChildNodes = n.getThenStmt().accept(this, graph);
        if(thenChildNodes != null){
            thenNode = graph.linkGraph(thenNode, thenChildNodes);
            ifNode.addChildNode(thenNode);
        }

        // examine the else stmt and get else node
        GraphNode elseNode = new GraphNode("Else");
        List<GraphNode> elseChildNodes = null;
        if(n.getElseStmt().isPresent()){
            elseChildNodes = n.getElseStmt().get().accept(this, graph);
        }
        if(elseChildNodes != null){
            elseNode = graph.linkGraph(elseNode, elseChildNodes);
            ifNode.addChildNode(elseNode);
        }

        graphNodes.add(ifNode);

        return graphNodes;
    }



    @Override
    public List<GraphNode> visit(LabeledStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * return statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ReturnStmt n, Graph graph) {
        System.out.println("[RETURN] " + n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        graphNodes.add(new GraphNode("Return"));

        return graphNodes;
    }

    /**
     * todo: examine switch statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(SwitchStmt n, Graph graph) {
        System.out.println("[SWITCH] " + n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: examine SynchronizedStmt
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(SynchronizedStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: examine ThrowStmt
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ThrowStmt n, Graph graph) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * try statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(TryStmt n, Graph graph) {
        System.out.println("[TRY]");
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode tryNode = new GraphNode("Try");

        // examine the try block and get try body node
        GraphNode tryBodyNode = new GraphNode("Body");
        List<GraphNode> tryBodyChildNodes = n.getTryBlock().accept(this, graph);
        if(tryBodyChildNodes != null){
            tryBodyNode = graph.linkGraph(tryBodyNode, tryBodyChildNodes);
            tryNode.addChildNode(tryBodyNode);
        }

        // examine the catch clauses and get catch nodes
        n.getCatchClauses().forEach(catchClause -> {
            GraphNode catchClausesNode = new GraphNode("Catch");
            List<GraphNode> catchClausesChildNodes = catchClause.accept(this, graph);
            if(catchClausesChildNodes != null){
                catchClausesNode = graph.linkGraph(catchClausesNode, catchClausesChildNodes);
                tryNode.addChildNode(catchClausesNode);
            }
        });

        // examine the try block and get try body node
        GraphNode finallyNode = new GraphNode("Finally");
        List<GraphNode> finallyChildNodes = null;
        if(n.getFinallyBlock().isPresent()){
            finallyChildNodes = n.getFinallyBlock().get().accept(this, graph);
        }
        if(finallyChildNodes != null){
            finallyNode = graph.linkGraph(finallyNode, finallyChildNodes);
            tryNode.addChildNode(finallyNode);
        }

        graphNodes.add(tryNode);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LocalClassDeclarationStmt n, Graph graph) {
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * while statement
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(WhileStmt n, Graph graph) {

        System.out.println("[WHILE]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode whileNode = new GraphNode("While");

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition");
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, graph);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            whileNode.addChildNode(conditionNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body");
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, graph);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            whileNode.addChildNode(bodyNode);
        }

        graphNodes.add(whileNode);

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

    /**
     * todo: ArrayAccessExpr
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayAccessExpr n, Graph graph) {
        System.out.println("    <ArrayAccessExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: ArrayCreationExpr
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayCreationExpr n, Graph graph) {
        System.out.println("    <ArrayCreationExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: ArrayInitializerExpr
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayInitializerExpr n, Graph graph) {
        System.out.println("    <ArrayInitializerExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * assign statement
     *
     * it's necessary to get the qualified name of the Target part(the left expression of operate)  of AssignExpr
     * we only need to get the qualified name of the Value part(the right expression of operate) of AssignExpr
     * AssignExpr: Target operator Value
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(AssignExpr n, Graph graph) {
        System.out.println("    <AssignExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        // we only need to get the qualified name of the Value part(the right expression of operate) of AssignExpr
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BinaryExpr n, Graph graph) {
        System.out.println("    <BinaryExpr> "+ n.toString());
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childeNodes = super.visit(n, graph);
        if(childeNodes != null){
            graphNodes.addAll(childeNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BooleanLiteralExpr n, Graph graph) {
        System.out.println("    <BooleanLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
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
            // todo: 将每一个graph加到前一个graph的子节点
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
        super.visit(n, graph);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LongLiteralExpr n, Graph graph) {
        System.out.println("    <LongLiteralExpr> "+ n.toString());
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;

    }

    /**
     * method call expression
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(MethodCallExpr n, Graph graph) {

        List<GraphNode> graphNodes = new ArrayList<>();
        /*
         * examine the method call inside the parameter list of the current method call expression at first
         *
         * obj.method1().method2()
         * output: method1() -> method2()
         *
         * obj.method1(method2())
         * output: method2() -> method1()
         */
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        System.out.println("    <MethodCallExpr> "+ n.toString());

        StringBuilder currentNodeName = new StringBuilder();
        String methodSignature;
        try{
            methodSignature = n.resolve().getQualifiedSignature();
        } catch (UnsolvedSymbolException e){
            methodSignature = "UnsolvedType.unsolvedMethod()";
        }
        currentNodeName.append(methodSignature);

        if(checkNodeName(currentNodeName.toString())){
            nodeNameList.add(currentNodeName.toString());
            graphNodes.add(new GraphNode(currentNodeName.toString(), "Method Call"));
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NameExpr n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();

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
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * object creation expression
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(ObjectCreationExpr n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();

        // examine the method call inside the constructor at first
        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        // get the qualified name of ObjectCreationExpr
        StringBuilder currentNodeName = new StringBuilder();
        String objCreationName;
        try{
            objCreationName = n.resolve().getQualifiedSignature();
            // currentNodeName.append(".").append("new").append("(").append(objCreationName, objCreationName.indexOf("(") + 1, objCreationName.indexOf(")") + 1 );

        } catch (UnsolvedSymbolException e){
            objCreationName = "UnresolvableType.new()";
        }

        currentNodeName.append(objCreationName);
        nodeNameList.add(currentNodeName.toString());
        graphNodes.add(new GraphNode(currentNodeName.toString(), "Object Creation"));

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

    /**
     * variable declaration expression
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(VariableDeclarationExpr n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);

        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));
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
        super.visit(n, graph);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * variable declarator
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(VariableDeclarator n, Graph graph) {
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);
        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));


        StringBuilder currentNodeName = new StringBuilder();
        // get the qualified type name of this variable
        String typeName;
        try{
            typeName = n.resolve().getType().describe();
        } catch (Exception e){
            typeName = "UnreslovedTypeX";
        }
        currentNodeName.append(typeName);

        // just declare a variable, but don't initialize it, for example: String str;
        if(!n.getInitializer().isPresent()){
            currentNodeName.append(".").append("Declaration");
            graphNodes.add(new GraphNode(currentNodeName.toString()));
            // todo: delete it
            nodeNameList.add(currentNodeName.toString());
        }

        /*
         * initialize the variable with a constant value
         * if the initial value is null, set the node "packageName.className.Declaration.NULL"
         */
        n.getInitializer().ifPresent(init -> {
            // only literalExpr will be added in this scope, other expressions (MethodCallExpr, ObjectCreationExpr) will be added in corresponding method
            if(init.isLiteralExpr() && !init.isNullLiteralExpr() && !init.isTextBlockLiteralExpr()){
                currentNodeName.append(".").append("Constant");
                graphNodes.add(new GraphNode(currentNodeName.toString()));
                nodeNameList.add(currentNodeName.toString());
            } else if(init.isNullLiteralExpr()){
                currentNodeName.append(".").append("Declaration").append(".").append("NULL");
                graphNodes.add(new GraphNode(currentNodeName.toString()));
                nodeNameList.add(currentNodeName.toString());
            }
        });

        return graphNodes;
    }

    /**
     * catch clause
     * @param n
     * @param graph
     * @return
     */
    @Override
    public List<GraphNode> visit(CatchClause n, Graph graph) {
        System.out.println("[CATCH]");

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, graph);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }


}
