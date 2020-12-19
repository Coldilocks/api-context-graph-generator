package visitor.tutorial;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author coldilock
 * 修改原始代码中字段初始化表达式的赋值
 */
public class ModifyingVisitor {
    private static final String FILE_PATH = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/test/resources/ReversePolishNotation.java";
    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        ModifierVisitor<?> numericLiteralVisitor = new IntegerLiteralModifier();
        numericLiteralVisitor.visit(cu, null);
        System.out.println(cu.toString());
    }

    /** A Simple Modifying Visitor */
    private static class IntegerLiteralModifier extends ModifierVisitor<Void> {

        @Override public FieldDeclaration visit(FieldDeclaration fd, Void arg) {

            super.visit(fd, arg);

            fd.getVariables().forEach(v ->
                    v.getInitializer().ifPresent(i -> {
                        if (i instanceof IntegerLiteralExpr) {
                            v.setInitializer(formatWithUnderscores(((IntegerLiteralExpr) i).getValue()));
                        }
                    }));

            return fd;
        }

    }

    private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

    static String formatWithUnderscores(String value){
        String withoutUnderscores = value.replaceAll("_", "");
        return LOOK_AHEAD_THREE.matcher(withoutUnderscores).replaceAll("$1_");
    }


}
