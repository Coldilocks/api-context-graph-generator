package visitors;

import entity.Graph;
import entity.GraphNode;
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
 * <h1>Method Visitor</h1>
 * visit every statement in a method, then generate an AST for this method
 * @author coldilock
 */
public class MethodVisitor extends GenericVisitorAdapterLite<GraphNode, String> {

    /** A util to create graph */
    public Graph graph;

    private boolean checkNodeName(String str){
        return !str.isEmpty() && !str.startsWith(".") && !str.startsWith("UnsolvedType.");
    }

    public MethodVisitor(Graph graph){
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

        // No Root Node in the final graph
        List<GraphNode> graphResult = new ArrayList<>();

        GraphNode rootNode;

        List<GraphNode> graphNodesWithoutRootNode = new ArrayList<>(graphNodes);
        if(graphNodes.size() > 0 && graphNodesWithoutRootNode.size() > 0){
            graphNodesWithoutRootNode.remove(0);
            rootNode = graph.linkNodesInControlFlow(graphNodes.get(0), graphNodesWithoutRootNode);
            graphResult.add(rootNode);
        }

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
     * if statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(IfStmt n, String nodeId) {

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode ifNode = new GraphNode("If", StringUtil.getUuid());

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkNodesInControlFlow(conditionNode, conditionChildNodes);
            ifNode.addChildNode(conditionNode);
            conditionNode.setParentNode(ifNode);
        }

        graph.addNewScope();

        // examine the then stmt and get then node
        GraphNode thenNode = new GraphNode("Then", StringUtil.getUuid());
        List<GraphNode> thenChildNodes = n.getThenStmt().accept(this, nodeId);
        if(thenChildNodes != null){
            thenNode = graph.linkNodesInControlFlow(thenNode, thenChildNodes);
            ifNode.addChildNode(thenNode);
            thenNode.setParentNode(ifNode);
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
            elseNode = graph.linkNodesInControlFlow(elseNode, elseChildNodes);
            ifNode.addChildNode(elseNode);
            elseNode.setParentNode(ifNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(ifNode);

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

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode whileNode = new GraphNode("While", StringUtil.getUuid());

        graph.addNewScope();

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkNodesInControlFlow(conditionNode, conditionChildNodes);
            whileNode.addChildNode(conditionNode);
            conditionNode.setParentNode(whileNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkNodesInControlFlow(bodyNode, bodyChildNodes);
            whileNode.addChildNode(bodyNode);
            bodyNode.setParentNode(whileNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(whileNode);

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

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode doWhileNode = new GraphNode("Do While", StringUtil.getUuid());

        // examine the condition expression and get condition node
        GraphNode conditionNode = new GraphNode("Condition", StringUtil.getUuid());
        List<GraphNode> conditionChildNodes = n.getCondition().accept(this, nodeId);
        if(conditionChildNodes != null){
            conditionNode = graph.linkNodesInControlFlow(conditionNode, conditionChildNodes);
            doWhileNode.addChildNode(conditionNode);
            conditionNode.setParentNode(doWhileNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        graph.addNewScope();
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkNodesInControlFlow(bodyNode, bodyChildNodes);
            doWhileNode.addChildNode(bodyNode);
            bodyNode.setParentNode(doWhileNode);
        }
        graph.jumpOutOfScope();

        graphNodes.add(doWhileNode);

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

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forNode = new GraphNode("For", StringUtil.getUuid());

        graph.addNewScope();

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization", StringUtil.getUuid());
        List<GraphNode> initializationChildNodes = n.getInitialization().accept(this, nodeId);
        if(initializationChildNodes != null){
            initializationNode = graph.linkNodesInControlFlow(initializationNode, initializationChildNodes);
            forNode.addChildNode(initializationNode);
            initializationNode.setParentNode(forNode);
        }

        // examine the compare expression and get compare node
        GraphNode compareNode = new GraphNode("Compare", StringUtil.getUuid());
        List<GraphNode> compareChildNodes = null;
        if(n.getCompare().isPresent()){
            compareChildNodes = n.getCompare().get().accept(this, nodeId);
        }
        if(compareChildNodes != null){
            compareNode = graph.linkNodesInControlFlow(compareNode, compareChildNodes);
            forNode.addChildNode(compareNode);
            compareNode.setParentNode(forNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkNodesInControlFlow(bodyNode, bodyChildNodes);
            forNode.addChildNode(bodyNode);
            bodyNode.setParentNode(forNode);
        }

        // examine the update expression and get update node
        GraphNode updateNode = new GraphNode("Update", StringUtil.getUuid());
        List<GraphNode> updateChildNodes = n.getUpdate().accept(this, nodeId);
        if(updateChildNodes != null){
            updateNode = graph.linkNodesInControlFlow(updateNode, updateChildNodes);
            forNode.addChildNode(updateNode);
            updateNode.setParentNode(forNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(forNode);

        return graphNodes;
    }

    /**
     * enhanced for statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ForEachStmt n, String nodeId) {

        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode forEachNode = new GraphNode("ForEach", StringUtil.getUuid());

        graph.addNewScope();

        // examine the initialization expression and get initialization node
        GraphNode initializationNode = new GraphNode("Initialization", StringUtil.getUuid());
        List<GraphNode> initializationChildNodes = n.getVariable().accept(this, nodeId);
        if(initializationChildNodes != null){
            initializationNode = graph.linkNodesInControlFlow(initializationNode, initializationChildNodes);
            forEachNode.addChildNode(initializationNode);
            initializationNode.setParentNode(forEachNode);
        }

        // examine the iterable expression and get iterable node
        GraphNode iterableNode = new GraphNode("Iterable", StringUtil.getUuid());
        List<GraphNode> iterableChildNodes = n.getIterable().accept(this, nodeId);
        if(iterableChildNodes != null){
            iterableNode = graph.linkNodesInControlFlow(iterableNode, iterableChildNodes);
            forEachNode.addChildNode(iterableNode);
            iterableNode.setParentNode(forEachNode);
        }

        // examine the body and get body node
        GraphNode bodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> bodyChildNodes = n.getBody().accept(this, nodeId);
        if(bodyChildNodes != null){
            bodyNode = graph.linkNodesInControlFlow(bodyNode, bodyChildNodes);
            forEachNode.addChildNode(bodyNode);
            bodyNode.setParentNode(forEachNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(forEachNode);

        return graphNodes;
    }

    /**
     * <h2>Switch Statement</h2>
     *
     * Switch statements are odd in terms of scoping:
     * <br><br>RIGHT:
     * <br>{@code switch(a) { case 0: int test = 0; System.out.println(test); case 1: test = 1; System.out.println(test);}}
     * <br><br>ERROR: The local variable value may not have been initialized.
     * <br>{@code switch(a) { case 0: int test = 0; System.out.println(test); case 1: System.out.println(test);}}
     *
     * <br><br>The 'int test' part tells the compiler at compile time that you have a variable called 'test' which is an int.
     * The 'test = 0' part initializes it, but that happens at run-time, and doesn't happen at all if that branch of the switch isn't entered.
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(SwitchStmt n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode switchNode = new GraphNode("Switch", StringUtil.getUuid());

        graph.addNewScope();

        // examine the selector and get selector node
        GraphNode selectorNode = new GraphNode("Selector", StringUtil.getUuid());
        List<GraphNode> selectorChildNodes = n.getSelector().accept(this, nodeId);
        if(selectorChildNodes != null){
            selectorNode = graph.linkNodesInControlFlow(selectorNode, selectorChildNodes);
            switchNode.addChildNode(selectorNode);
            selectorNode.setParentNode(switchNode);
        }

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine statements inside every case (SwitchEntry in JavaParser)
        n.getEntries().forEach(switchEntry -> {
            String caseOrDefaultLabel = "";
            if(switchEntry.toString().startsWith("default:") || switchEntry.toString().startsWith("default :"))
                caseOrDefaultLabel = "Default";
            else
                caseOrDefaultLabel = "Case";
            GraphNode caseNode = new GraphNode(caseOrDefaultLabel, StringUtil.getUuid());
            List<GraphNode> caseChildNodes = switchEntry.accept(this, nodeId);
            if(caseChildNodes != null){
                caseNode = graph.linkNodesInControlFlow(caseNode, caseChildNodes);
                switchNode.addChildNode(caseNode);
                caseNode.setParentNode(switchNode);
            }
        });

        graph.jumpOutOfScope();

        graphNodes.add(switchNode);
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SwitchEntry n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        // Get childNodes from statements inside case
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    /**
     * Switch Expression
     * In Java 12, 'switch' can also be used as an expression
     * The process is same as SwitchStmt
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(SwitchExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        GraphNode switchNode = new GraphNode("Switch", StringUtil.getUuid());

        graph.addNewScope();

        // examine the selector and get selector node
        GraphNode selectorNode = new GraphNode("Selector", StringUtil.getUuid());
        List<GraphNode> selectorChildNodes = n.getSelector().accept(this, nodeId);
        if(selectorChildNodes != null){
            selectorNode = graph.linkNodesInControlFlow(selectorNode, selectorChildNodes);
            switchNode.addChildNode(selectorNode);
            selectorNode.setParentNode(switchNode);
        }

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine statements inside every case (SwitchEntry in JavaParser)
        n.getEntries().forEach(switchEntry -> {
            GraphNode caseNode = new GraphNode("Case", StringUtil.getUuid());
            List<GraphNode> caseChildNodes = switchEntry.accept(this, nodeId);
            if(caseChildNodes != null){
                caseNode = graph.linkNodesInControlFlow(caseNode, caseChildNodes);
                switchNode.addChildNode(caseNode);
                caseNode.setParentNode(switchNode);
            }
        });

        graph.jumpOutOfScope();

        graphNodes.add(switchNode);
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
        List<GraphNode> graphNodes = new ArrayList<>();
        GraphNode tryNode = new GraphNode("Try", StringUtil.getUuid());

        graph.addNewScope();

        // examine the try block and get try body node
//        GraphNode tryBodyNode = new GraphNode("Body", StringUtil.getUuid());
//        List<GraphNode> tryBodyChildNodes = n.getTryBlock().accept(this, nodeId);
//        if(tryBodyChildNodes != null){
//            tryBodyNode = graph.linkNodesInControlFlow(tryBodyNode, tryBodyChildNodes);
//            tryNode.addChildNode(tryBodyNode);
//        }
//        GraphNode tryBodyNode = new GraphNode("Body", StringUtil.getUuid());
        List<GraphNode> tryBodyChildNodes = n.getTryBlock().accept(this, nodeId);
        if(tryBodyChildNodes != null){
            GraphNode tryBodyNode = tryBodyChildNodes.get(0);
            tryBodyChildNodes.remove(0);
            tryBodyNode = graph.linkNodesInControlFlow(tryBodyNode, tryBodyChildNodes);
            tryNode.addChildNode(tryBodyNode);
            tryBodyNode.setParentNode(tryNode);
        }

        graph.jumpOutOfScope();

        graph.addNewScope();

        // examine the catch clauses and get catch nodes
        n.getCatchClauses().forEach(catchClause -> {
            GraphNode catchClausesNode = new GraphNode("Catch", StringUtil.getUuid());
            List<GraphNode> catchClausesChildNodes = catchClause.accept(this, nodeId);
            if(catchClausesChildNodes != null){
                catchClausesNode = graph.linkNodesInControlFlow(catchClausesNode, catchClausesChildNodes);
                tryNode.addChildNode(catchClausesNode);
                catchClausesNode.setParentNode(tryNode);
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
            finallyNode = graph.linkNodesInControlFlow(finallyNode, finallyChildNodes);
            tryNode.addChildNode(finallyNode);
            finallyNode.setParentNode(tryNode);
        }

        graph.jumpOutOfScope();

        graphNodes.add(tryNode);
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
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    /**
     * Throw Statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ThrowStmt n, String nodeId) {
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

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        graphNodes.add(new GraphNode("Return", StringUtil.getUuid()));

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

    @Override
    public List<GraphNode> visit(LabeledStmt n, String nodeId) {
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

    @Override
    public List<GraphNode> visit(LocalClassDeclarationStmt n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
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
     * expression statement
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ExpressionStmt n, String nodeId) {

        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childNodes = super.visit(n, nodeId);

        graphNodes.addAll(CollectionUtils.emptyIfNull(childNodes));

        return graphNodes;
    }

    /**
     * Array Access Expression
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayAccessExpr n, String nodeId) {

        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * Array Creation Expression
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayCreationExpr n, String nodeId) {

        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * Array Initializer Expression
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(ArrayInitializerExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    /**
     * key of data dependency
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(NameExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        graph.linkDataFlow(nodeId, n.getNameAsString());

        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null)
            graphNodes.addAll(childNodes);

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
        } catch (UnsolvedSymbolException e){
            typeName = "UnsolvedType.UnsolvedSymbolException.In.VariableDeclarator.varType";
        } catch (RuntimeException e){
            typeName = "UnsolvedType.RuntimeException.In.VariableDeclarator.varType";
        } catch (Exception e){
            typeName = "UnsolvedType.In.VariableDeclarator.varType";
        }
        currentNodeName.append(typeName);

        // unsolved type variable will not be recorded
        if(!checkNodeName(typeName))
            return graphNodes;

        /*
         * CREATE node for initializer-ABSENT VariableDeclarator
         *  just declare a variable, but don't initialize it, for example: String str;
         */
        if(!n.getInitializer().isPresent()){
            currentNodeName.append(".").append("Declaration");
            graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "VarDec", n.toString(), currentNodeId));
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
                graph.addNewVarInCurrentScope(n.getNameAsString(), currentNodeId);
            } else if(init.isNullLiteralExpr()){
                currentNodeName.append(".").append("Declaration").append(".").append("Null");
                graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "VarDec", n.toString(), currentNodeId));
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
            objCreationName = "UnsolvedType.UnsolvedSymbolException.In.ObjectCreationExpr.new()";
        } catch (RuntimeException e){
            objCreationName = "UnsolvedType.RuntimeException.In.ObjectCreationExpr.new()";
        } catch (Exception e){
            objCreationName = "UnsolvedType.In.ObjectCreationExpr.new()";
        }
        currentNodeName.append(objCreationName);

        if(n.getParentNode().isPresent()){

            String originalStmt = n.getParentNode().get().toString();

            String varIdentifier = null;

            // parentNode could be VariableDeclarator or AssignExpr
            try{
                varIdentifier = ((VariableDeclarator) n.getParentNode().get()).getNameAsString();
            } catch (ClassCastException ignored){
            }

            try{
                varIdentifier = ((AssignExpr) n.getParentNode().get()).getTarget().toString();
            } catch (ClassCastException ignored){
            }

            if(varIdentifier != null){
                graphNodes.add(new GraphNode(StringUtil.replaceName(currentNodeName.toString()), varIdentifier, "ObjCreation", originalStmt, currentNodeId));
            } else {
                graphNodes.add(new GraphNode(StringUtil.replaceName(currentNodeName.toString()), "ObjCreation", originalStmt, currentNodeId));
            }

        } else {
            // e.g. 'new File();'
            graphNodes.add(new GraphNode(StringUtil.replaceName(currentNodeName.toString()), "ObjCreation", n.toString(), currentNodeId));
        }

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
        List<GraphNode> childNodes = super.visit(n, currentNodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        StringBuilder currentNodeName = new StringBuilder();
        String methodSignature;
        try{
            methodSignature = n.resolve().getQualifiedSignature();
        } catch (UnsolvedSymbolException e){
            methodSignature = "UnsolvedType.UnsolvedSymbolException.In.MethodCallExpr.method()";
        } catch (RuntimeException e){
            methodSignature = "UnsolvedType.RuntimeException.In.MethodCallExpr.method()";
        } catch (Exception e){
            methodSignature = "UnsolvedType.In.MethodCallExpr.method()";
        }
        currentNodeName.append(methodSignature);

        if(checkNodeName(currentNodeName.toString())){
//            graphNodes.add(new GraphNode(currentNodeName.toString(), n.getNameAsString(), "MethodCall", n.toString(), currentNodeId));
            // method name will not be recorded
            graphNodes.add(new GraphNode(currentNodeName.toString(), "MethodCall", n.toString(), currentNodeId));
        }

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

    @Override
    public List<GraphNode> visit(BooleanLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CharLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(DoubleLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(IntegerLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LongLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NullLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(StringLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TextBlockLiteralExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(CastExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ClassExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ConditionalExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(EnclosedExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(FieldAccessExpr n, String nodeId) {
//        super.visit(n, nodeId);
//        List<GraphNode> graphNodes = new ArrayList<>();
//        return graphNodes;

        List<GraphNode> graphNodes = new ArrayList<>();

        // fieldAccess like "this.xxx" will not be recorded
        if(n.getScope().isThisExpr()){
            return graphNodes;
        }

        /*
         * 'System.out.println("something")' -> System.out.println() is the parent node of System.out in JavaParser
         * We won't create a node for System.out in this case for our API graph.
         */
        if(n.getParentNode().isPresent()){
            try{
                MethodCallExpr m = (MethodCallExpr) n.getParentNode().get();
                return graphNodes;
            } catch (ClassCastException ignored){
            }
        }

        String currentNodeId = StringUtil.getUuid();

        List<GraphNode> childNodes = super.visit(n, currentNodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        StringBuilder currentNodeName = new StringBuilder();
        String filedName;
        try{
            filedName = n.resolve().getType().describe();
        } catch (UnsolvedSymbolException e){
            filedName = "UnsolvedType.UnsolvedSymbolException.In.FieldAccessExpr.fieldType";
        } catch (RuntimeException e){
            filedName = "UnsolvedType.RuntimeException.In.FieldAccessExpr.fieldType";
        } catch (Exception e){
            filedName = "UnsolvedType.In.FieldAccessExpr.fieldType";
        }
        currentNodeName.append(filedName);

        if(checkNodeName(currentNodeName.toString())){
            graphNodes.add(new GraphNode(currentNodeName.toString(), "FieldAccess", n.toString(), currentNodeId));
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(InstanceOfExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(BinaryExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();

        List<GraphNode> childeNodes = super.visit(n, nodeId);
        if(childeNodes != null){
            graphNodes.addAll(childeNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(NormalAnnotationExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SingleMemberAnnotationExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(SuperExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(UnaryExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }

        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(ThisExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(LambdaExpr n, String nodeId) {
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphNode> childNodes = super.visit(n, nodeId);
        if(childNodes != null){
            graphNodes.addAll(childNodes);
        }
        return graphNodes;
    }

    /**
     * todo: method reference
     * @param n
     * @param nodeId
     * @return
     */
    @Override
    public List<GraphNode> visit(MethodReferenceExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

    @Override
    public List<GraphNode> visit(TypeExpr n, String nodeId) {
        super.visit(n, nodeId);
        List<GraphNode> graphNodes = new ArrayList<>();
        return graphNodes;
    }

}
