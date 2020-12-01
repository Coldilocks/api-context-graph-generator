import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author coldilock
 */
public class CommentReporterComplete {

    private static final String FILE_PATH = "src/main/java/org/javaparser/samples/ReversePolishNotation.java";

    private static class CommentReportEntry {
        private String type;
        private String text;
        private int lineNumber;
        private boolean isOrphan;

        CommentReportEntry(String type, String text, int lineNumber, boolean isOrphan) {
            this.type = type;
            this.text = text;
            this.lineNumber = lineNumber;
            this.isOrphan = isOrphan;
        }

        @Override public String toString() {
            return lineNumber + "|" + type + "|" + isOrphan + "|" + text.replaceAll("\\n","").trim();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

//        List<Comment> comments = cu.getAllContainedComments();

        List<CommentReportEntry> comments = cu.getAllContainedComments()
                .stream() .map(p -> new CommentReportEntry(p.getClass().getSimpleName(),
                        p.getContent(),
                        p.getRange().get().begin.line,
                        !p.getCommentedNode().isPresent())) .collect(Collectors.toList());

        comments.forEach(System.out::println);
    }
}
