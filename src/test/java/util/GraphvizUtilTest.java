package util;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for {@link GraphvizUtil}
 * @author coldilock
 */
public class GraphvizUtilTest {

    @Test
    public void createGraph() throws IOException {
        String inputJavaFilePath = "src/test/resources/testcase/Task1.java";
        String outputDotFilePath = "src/test/resources/dot/Task1.dot";
//        GraphvizUtil.createDotFile(inputJavaFilePath, outputDotFilePath);
    }

}
