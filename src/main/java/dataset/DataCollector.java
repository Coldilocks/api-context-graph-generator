package dataset;

import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class DataCollector {
    public static List<List<String>> graphVocabList = new ArrayList<>();
    public static List<List<String>> graphReprensentList = new ArrayList<>();
    public static List<String> traceList = new ArrayList<>();
    public static List<String> singlePredictionList = new ArrayList<>();
    public static List<String> blockPredictionList = new ArrayList<>();
    public static List<String> holeSizeList = new ArrayList<>();
    public static List<String> variableNameList = new ArrayList<>();
    public static List<String> originalStatementList= new ArrayList<>();

    public static void createDataSet(){
        String fileFolder = "/Users/coldilock/Downloads/output/";
        FileUtil.save2dListFile(graphVocabList, fileFolder + "graph_vocab.txt");
        FileUtil.save2dListFile(graphReprensentList, fileFolder + "graph_reprensent.txt");
        FileUtil.saveListFile(traceList, fileFolder + "trace.txt");
        FileUtil.saveListFile(singlePredictionList, fileFolder + "single_prediction.txt");
        FileUtil.saveListFile(blockPredictionList, fileFolder + "block_prediction.txt");
        FileUtil.saveListFile(holeSizeList, fileFolder + "hole_size.txt");
        FileUtil.saveListFile(variableNameList, fileFolder + "variable_names.txt");
        FileUtil.saveListFile(originalStatementList, fileFolder + "original_statement.txt");
    }
}
