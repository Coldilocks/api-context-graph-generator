package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author coldilock
 */
public class DataConfig {
    /** 输入的Java文件路径 */
    public static String JAVA_FILE_PATH;
    /** 输出的所有文件路径 */
    public static String OUTPUT_PATH;
    /** 生成的所有图的路径 */
    public static String GRAPH_OUTPUT_PATH;
    /** glove词表 */
    public static String GLOVE_VOCAB_PATH = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/main/resources/vocab/gloveVocab.txt";
    /** 常用停用词表 */
    public static String STOP_WORDS_PATH = "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/main/resources/vocab/stopWords.txt";
    /** 进行预测的python代码路径 */
    public static String GGNN_CLIENT_PYTHON_FILE_PATH;
    /** 分词服务访问路径 */
    public static String URL;
    /** 输入的Java文件（测试用）*/
    public static String TEST_INPUT_JAVA_FILE;
    /** 输出的图（测试用）*/
    public static String TEST_OUTPUT_GRAPH_PATH;

    private static final Properties PROPERTIES = new Properties();

    /**
     * 加载配置
     */
    public static void loadConfig(String configFile){
        try{
            PROPERTIES.load(new FileReader(configFile));
            JAVA_FILE_PATH = PROPERTIES.getProperty("javaFilePath");
            OUTPUT_PATH = PROPERTIES.getProperty("outputPath");
            GRAPH_OUTPUT_PATH = PROPERTIES.getProperty("graphOutputPath");
            GLOVE_VOCAB_PATH = PROPERTIES.getProperty("gloveVocab");
            STOP_WORDS_PATH = PROPERTIES.getProperty("stopWords");
            GGNN_CLIENT_PYTHON_FILE_PATH = PROPERTIES.getProperty("ggnnClient");
            URL = PROPERTIES.getProperty("url");
            TEST_INPUT_JAVA_FILE = PROPERTIES.getProperty("testInputJavaFile");
            TEST_OUTPUT_GRAPH_PATH = PROPERTIES.getProperty("testOutputGraphPath");
        } catch (IOException e){
            System.exit(0);
        }
    }
}
