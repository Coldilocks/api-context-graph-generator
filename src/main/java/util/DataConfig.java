package util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author coldilock
 */
public class DataConfig {
    /** 包含所有训练数据（Java项目）的根目录 */
    public static String DATASET_ROOT;
    /** 存放Java文件列表的txt文件路径 */
    public static String JAVA_FILE_PATH;
    /** 包含所有".jar"文件的目录*/
    public static String JAR_FILE_ROOT;
    /** 存放Jar包路径列表的txt文件路径 **/
    public static String JAR_FILE_PATH;
    /** 数据集（txt格式）的输出路径 */
    public static String OUTPUT_PATH;
    /** 数据集（可选，图片格式）的输出路径 */
    public static String GRAPH_OUTPUT_PATH;
    /** glove词表 */
    public static String GLOVE_VOCAB_PATH = "src/main/resources/vocab/gloveVocab.txt";
    /** 常用停用词表 */
    public static String STOP_WORDS_PATH = "src/main/resources/vocab/stopWords.txt";
    /** 进行预测的python代码路径 */
    public static String GGNN_CLIENT_PYTHON_FILE_PATH;
    /** 分词服务访问路径 */
    public static String URL;
    /** （测试）输入的Java文件 */
    public static String TEST_INPUT_JAVA_FILE;
    /** （测试）输出的图 */
    public static String TEST_OUTPUT_GRAPH_PATH;

    private static final Properties PROPERTIES = new Properties();

    /**
     * 加载配置
     */
    public static void loadConfig(String configFile){
        try{
            PROPERTIES.load(new FileReader(configFile));
            DATASET_ROOT = PROPERTIES.getProperty("datasetRoot");
            JAVA_FILE_PATH = PROPERTIES.getProperty("javaFilePath");
            JAR_FILE_ROOT = PROPERTIES.getProperty("jarFileRoot");
            JAR_FILE_PATH = PROPERTIES.getProperty("jarFilePath");
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
