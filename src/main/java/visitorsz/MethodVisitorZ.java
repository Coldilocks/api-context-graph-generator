package visitorsz;

import codeanalysis.representation.Graph;
import codeanalysis.representation.GraphNode;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import org.apache.commons.collections4.CollectionUtils;
import util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class MethodVisitorZ extends GenericVisitorAdapterZ<GraphNode, String> {
    
    public Graph graph = new Graph();

    public List<String> nodeNameList = new ArrayList<>();


    private boolean checkNodeName(String str){
        return !str.isEmpty() && !str.startsWith(".");
    }

    public MethodVisitorZ(Graph graph){
        this.graph = graph;
    }

    @Override
    public List<GraphNode> visit(MethodDeclaration n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        n.getBody().ifPresent(blockStmt -> {
            graph.addNewScope();
            blockStmt.getStatements().forEach(statement -> {
                List<GraphNode> childNodes = statement.accept(this, nodeId);
                graphNodes.addAll(childNodes);
            });
            graph.jumpOutOfScope();
        });

        GraphNode rootNode = new GraphNode("Root", StringUtil.getUuid());
        rootNode = graph.linkGraph(rootNode, graphNodes);

        List<GraphNode> graphResult = new ArrayList<>();
        graphResult.add(rootNode);
        return graphResult;

    }

    @Override
    public List<GraphNode> visit(AssertStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BlockStmt n, String nodeId) {
        System.out.println("[BLOCK]");
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BreakStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ContinueStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * do while statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(DoStmt n, String nodeId) {
        System.out.println("[DO WHILE]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode doWhileNode = new GraphNode("doWhile", StringUtil.getUuid());

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            doWhileNode.addChildNode(conditionNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        graph.addNewScope();
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            doWhileNode.addChildNode(bodyNode);
        }
        graph.jumpOutOfScope();

        graphNodes.add(doWhileNode);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(EmptyStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ExplicitConstructorInvocationStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * expression statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ExpressionStmt n, String nodeId) {

        System.out.println("\n[EXPRESSION] " + n.getExpression().toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);

        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));

        return graphNodes;
    }

    /**
     * enchaned for statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ForEachStmt n, String nodeId) {
        System.out.println("[FOR EACH]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forEachNode = new GraphNode("ForEach", StringUtil.getUuid());

        graph.addNewScope();

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization", StringUtil.getUuid());
        List<GraphNode> initializationChildNodes = n.getVariable().accept(this, nodeId);
        if(initializationChildNodes != null){
            initializationNode = graph.linkGraph(initializationNode, initializationChildNodes);
            forEachNode.addChildNode(initializationNode);
        }

        // examine the iterable expression and get iterable node
        GraphNode iterableNode = new GraphNode("Iterable", StringUtil.getUuid());
        List<GraphNode> iterableChildNodes = n.getIterable().accept(this, nodeId);
        if(iterableChildNodes != null){
            iterableNode = graph.linkGraph(iterableNode, iterableChildNodes);
            forEachNode.addChildNode(iterableNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            forEachNode.addChildNode(bodyNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(forEachNode);

        return graphNodes;
    }

    /**
     * for statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ForStmt n, String nodeId) {
        System.out.println("[FOR]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forNode = new GraphNode("For", StringUtil.getUuid());

        graph.addNewScope();

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization", StringUtil.getUuid());
        List<GraphNode> initializationChildNodes = n.getInitialization().accept(this, nodeId);
        if(initializationChildNodes != null){
            initializationNode = graph.linkGraph(initializationNode, initializationChildNodes);
            forNode.addChildNode(initializationNode);
        }

        // examine the compare expression and get compare node
        GraphNode compareNode = new GraphNode("Compare", StringUtil.getUuid());
        List<GraphNode> compareChildNodes = null;
        if(n.getCompare().isPresent()){
            compareChildNodes = n.getCompare().get().accept(this, nodeId);
        }
        if(compareChildNodes != null){
            compareNode = graph.linkGraph(compareNode, compareChildNodes);
            forNode.addChildNode(compareNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            forNode.addChildNode(bodyNode);
        }

        // examine the update expression and get update node
        GraphNode updateNode = new GraphNode("Update", StringUtil.getUuid());
        List<GraphNode> updateChildNodes = n.getUpdate().accept(this, nodeId);
        if(updateChildNodes != null){
            updateNode = graph.linkGraph(updateNode, updateChildNodes);
            forNode.addChildNode(updateNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(forNode);

        return graphNodes;
    }

    /**
     * if statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(IfStmt n, String nodeId) {
        System.out.println("[IF]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode ifNode = new GraphNode("If", StringUtil.getUuid());

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            ifNode.addChildNode(conditionNode);
        }

        graph.addNewScope();

        // examine the then stmt and get then node
        GraphNode thenNode = new GraphNode("Then", StringUtil.getUuid());
        List<GraphNode> thenChildNodes = n.getThenStmt().accept(this, nodeId);
        if(thenChildNodes != null){
            thenNode = graph.linkGraph(thenNode, thenChildNodes);
            ifNode.addChildNode(thenNode);
        }

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine the else stmt and get else node
        GraphNode elseNode = new GraphNode("Else", StringUtil.getUuid());
        List<GraphNode> elseChildNodes = null;
        if(n.getElseStmt().isPresent()){
            elseChildNodes = n.getElseStmt().get().accept(this, nodeId);
        }
        if(elseChildNodes != null){
            elseNode = graph.linkGraph(elseNode, elseChildNodes);
            ifNode.addChildNode(elseNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(ifNode);

        return graphNodes;
    }



    @Override
    public List<GraphNode> visit(LabeledStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * return statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ReturnStmt n, String nodeId) {
        System.out.println("[RETURN] " + n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        graphNodes.add(new GraphNode("Return", StringUtil.getUuid()));

        return graphNodes;
    }

    /**
     * todo: examine switch statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(SwitchStmt n, String nodeId) {
        System.out.println("[SWITCH] " + n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: examine SynchronizedStmt
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(SynchronizedStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: examine ThrowStmt
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ThrowStmt n, String nodeId) {
        System.out.println("[THROW] " + n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * try statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(TryStmt n, String nodeId) {
        System.out.println("[TRY]");
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode tryNode = new GraphNode("Try", StringUtil.getUuid());

        graph.addNewScope();

        // examine the try block and get try body node
        GraphNode tryBodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> tryBodyChildNodes = n.getTryBlock().accept(this, nodeId);
        if(tryBodyChildNodes != null){
            tryBodyNode = graph.linkGraph(tryBodyNode, tryBodyChildNodes);
            tryNode.addChildNode(tryBodyNode);
        }

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine the catch clauses and get catch nodes
        n.getCatchClauses().forEach(catchClause -> {
            GraphNode catchClausesNode = new GraphNode("Catch", StringUtil.getUuid());
            List<GraphNode> catchClausesChildNodes = catchClause.accept(this, nodeId);
            if(catchClausesChildNodes != null){
                catchClausesNode = graph.linkGraph(catchClausesNode, catchClausesChildNodes);
                tryNode.addChildNode(catchClausesNode);
            }
        });

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine the try block and get try body node
        GraphNode finallyNode = new GraphNode("Finally", StringUtil.getUuid());
        List<GraphNode> finallyChildNodes = null;
        if(n.getFinallyBlock().isPresent()){
            finallyChildNodes = n.getFinallyBlock().get().accept(this, nodeId);
        }
        if(finallyChildNodes != null){
            finallyNode = graph.linkGraph(finallyNode, finallyChildNodes);
            tryNode.addChildNode(finallyNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(tryNode);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LocalClassDeclarationStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * while statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(WhileStmt n, String nodeId) {

        System.out.println("[WHILE]");

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode whileNode = new GraphNode("While", StringUtil.getUuid());

        graph.addNewScope();

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkGraph(conditionNode, conditionChildNodes);
            whileNode.addChildNode(conditionNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkGraph(bodyNode, bodyChildNodes);
            whileNode.addChildNode(bodyNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(whileNode);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(UnparsableStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(YieldStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: ArrayAccessExpr
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayAccessExpr n, String nodeId) {
        System.out.println("    <ArrayAccessExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: ArrayCreationExpr
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayCreationExpr n, String nodeId) {
        System.out.println("    <ArrayCreationExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * todo: ArrayInitializerExpr
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayInitializerExpr n, String nodeId) {
        System.out.println("    <ArrayInitializerExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * assign expression
     *
     * AssignExpr: Target operator Value, e.g. {@code str1 = "hello world";}
     * Target: 'str1', operator: '=', Value: "hello world"
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(AssignExpr n, String nodeId) {
        System.out.println("    <AssignExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();
        /*
         * Examine the value part of AssignExpr(i.e. the RIGHT expression of operate).
         *
         * AssignValueNodes may contains several nodes:
         *      e.g. 'str1 = str1.replace(sb.toString, str2));'
         *      two nodes: 'java.lang.StringBuffer.toString()' & 'java.lang.String.replace(java.lang.String, java.lang.String)'
         *
         * We use the last node's id to represent the id of the whole AssignExpr,
         * the id will be used in data dependency process phase.
         *      e.g. 'String str1;\n    str1 = str1.replace(sb.toString, str2));'
         *      the data flow of str1: 'java.lang.String.Declaration' -> 'java.lang.String.replace(java.lang.String, java.lang.String)'
         */
        List<GraphNode> assignValueNodes = n.getValue().accept(this, nodeId);
        String lastNodeId = "";
        if(assignValueNodes != null && assignValueNodes.size() > 0){
            lastNodeId = assignValueNodes.get(assignValueNodes.size() - 1).getId();
            graphNodes.addAll(assignValueNodes);
        }

        List<GraphNode> childNodes_ = n.getTarget().accept(this, lastNodeId);
        if(childNodes_ != null){
            graphNodes.addAll(childNodes_);
        }

        return graphNodes;
    }
//    @Override
//    public List<GraphNode> visit(AssignExpr n, String nodeId) {
//        System.out.println("    <AssignExpr> "+ n.toString());
//
//        List<GraphNode> graphNodes = new ArrayList<>();
//
//        // we only need to get the qualified name of the Value part(the right expression of operate) of AssignExpr
//        List<GraphNode> childNodes = super.visit(n, nodeId);
//        if(childNodes != null){
//            graphNodes.addAll(childNodes);
//        }
//
//        return graphNodes;
//    }

    @Override
    public List<GraphNode> visit(BinaryExpr n, String nodeId) {
        System.out.println("    <BinaryExpr> "+ n.toString());
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childeNodes = super.visit(n, nodeId);
        if(childeNodes != null){
            graphNodes.addAll(childeNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BooleanLiteralExpr n, String nodeId) {
        System.out.println("    <BooleanLiteralExpr> "+ n.toString());
        // System.out.println("        $ .Constant");
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CastExpr n, String nodeId) {
        System.out.println("    <CastExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CharLiteralExpr n, String nodeId) {
        System.out.println("    <CharLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ClassExpr n, String nodeId) {
        System.out.println("    <ClassExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ConditionalExpr n, String nodeId) {
        System.out.println("    <ConditionalExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(DoubleLiteralExpr n, String nodeId) {
        System.out.println("    <DoubleLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(EnclosedExpr n, String nodeId) {
        System.out.println("    <EnclosedExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(FieldAccessExpr n, String nodeId) {
        System.out.println("    <FieldAccessExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(InstanceOfExpr n, String nodeId) {
        System.out.println("    <InstanceOfExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(IntegerLiteralExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();
        System.out.println("    <IntegerLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LongLiteralExpr n, String nodeId) {
        System.out.println("    <LongLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;

    }


    /**
     * method call expression
     * @param n
     * @param nodeId id of MethodCallExpr's parent node
     * @return
     */
    @Override
    public List<GraphNode> visit(MethodCallExpr n, String nodeId) {

        List<GraphNode> graphNodes = new ArrayList<>();

        String currentNodeId = StringUtil.getUuid();

        /*
         * examine the method call inside the parameter list of the current method call expression at first
         *
         * obj.method1().method2()
         * output: method1() -> method2()
         *
         * obj.method1(method2())
         * output: method2() -> method1()
         */
        // todo: pass currentNodeId to n.getArguments, pass nodeId to n.getScope
        List<GraphNode> childNodes = super.visit(n, currentNodeId);
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
            graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "MethodCall", n.toString(), currentNodeId));
        }

        return graphNodes;
    }

    /**
     * key of data dependency
     * @param n®
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(NameExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        System.out.println("Name: "+n.toString());

        graph.linkDataFlow(nodeId, n.getNameAsString());

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NormalAnnotationExpr n, String nodeId) {
        System.out.println("    <NormalAnnotationExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NullLiteralExpr n, String nodeId) {
        System.out.println("    <NullLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * object creation expression
     * @param n
     * @param nodeId id of ObjectCreationExpr's parent node
     * @return
     */
    @Override
    public List<GraphNode> visit(ObjectCreationExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        String currentNodeId = StringUtil.getUuid();

        // Examine the method call inside the constructor at first
        // todo: pass currentNodeId to n.getArguments, pass nodeId to n.getScope
        List<GraphNode> childNodes = super.visit(n, currentNodeId);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

        // Get the qualified name of ObjectCreationExpr
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

        if(n.getParentNode().isPresent()){
            String originalStmt = n.getParentNode().get().toString();
            String varIdentifier = ((VariableDeclarator) n.getParentNode().get()).getNameAsString();
            graphNodes.add(new GraphNode(currentNodeName.toString(), varIdentifier, "ObjCreation", originalStmt, currentNodeId));

        } else {
            // e.g. 'new File();'
            graphNodes.add(new GraphNode(currentNodeName.toString(), "unknown", "ObjCreation", n.toString(), currentNodeId));
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SingleMemberAnnotationExpr n, String nodeId) {
        System.out.println("    <SingleMemberAnnotationExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(StringLiteralExpr n, String nodeId) {
        System.out.println("    <StringLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SuperExpr n, String nodeId) {
        System.out.println("    <SuperExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(UnaryExpr n, String nodeId) {
        System.out.println("    <UnaryExpr> "+ n.toString());

        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ThisExpr n, String nodeId) {
        System.out.println("    <ThisExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * variable declaration expression
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(VariableDeclarationExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);

        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LambdaExpr n, String nodeId) {
        System.out.println("    <LambdaExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(MethodReferenceExpr n, String nodeId) {
        System.out.println("    <MethodReferenceExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TypeExpr n, String nodeId) {
        System.out.println("    <TypeExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SwitchExpr n, String nodeId) {
        System.out.println("    <SwitchExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TextBlockLiteralExpr n, String nodeId) {
        System.out.println("    <TextBlockLiteralExpr> "+ n.toString());
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * variable declarator
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(VariableDeclarator n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        // This id will only be used when the Initializer is LiteralExpr
        String currentNodeId = StringUtil.getUuid();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));

        StringBuilder currentNodeName = new StringBuilder();
        // Get the qualified type name of this variable
        String typeName;
        try{
            typeName = n.resolve().getType().describe();
        } catch (Exception e){
            typeName = "UnreslovedTypeX";
        }
        currentNodeName.append(typeName);

        /*
         * CREATE node for initializer-ABSENT VariableDeclarator
         *  just declare a variable, but don't initialize it, for example: String str;
         */
        if(!n.getInitializer().isPresent()){
            currentNodeName.append(".").append("Declaration");
            graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "VarDec", n.toString(), currentNodeId));
            nodeNameList.add(currentNodeName.toString());
            graph.addNewVarInCurrentScope(n.getNameAsString(), currentNodeId);
        }

        /*
         * CREATE node for LITERAL initializer of VariableDeclarator
         *  i.e. initialize the variable with a constant value, including NULL initializer
         *  if the initial value is null, node's name will be "packageName.className.Declaration.NULL"
         *
         * nodes for OTHER expr (MethodCallExpr, ObjectCreationExpr, etc.) will be created in corresponding method
         */
        n.getInitializer().ifPresent(init -> {
            if(init.isLiteralExpr() && !init.isNullLiteralExpr() && !init.isTextBlockLiteralExpr()){
                currentNodeName.append(".").append("Constant");
                graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "VarDec", n.toString(), currentNodeId));
                nodeNameList.add(currentNodeName.toString());
                graph.addNewVarInCurrentScope(n.getNameAsString(), currentNodeId);
            } else if(init.isNullLiteralExpr()){
                currentNodeName.append(".").append("Declaration").append(".").append("NULL");
                graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "VarDec", n.toString(), currentNodeId));
                nodeNameList.add(currentNodeName.toString());
                graph.addNewVarInCurrentScope(n.getNameAsString(), currentNodeId);
            } else if((init.isMethodCallExpr() || init.isObjectCreationExpr()) && childNodes.size() > 0){
                /*
                 * Record variable name to SYMBOLTABLE.
                 *
                 * when the initializer is a METHODCALL / OBJECTCREATION Expr,
                 * the corresponding node has already been created and added into the childNodes list (the LAST node).
                 * So get the node id and add the (varName, nodeId) pair to SYMBOLTABLE (used for data dependency phase)
                 */
                String lastNodeId = childNodes.get(childNodes.size() - 1).getId();
                graph.addNewVarInCurrentScope(n.getNameAsString(), lastNodeId);
            }
        });

        return graphNodes;
    }

    /**
     * catch clause
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(CatchClause n, String nodeId) {
        System.out.println("[CATCH]");

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

}
