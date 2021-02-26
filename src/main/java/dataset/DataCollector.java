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

    public static void createDataSet(){
        String fileFolder = "/Users/coldilock/Downloads/output/";
        FileUtil.save2dListFile(graphVocabList, fileFolder + "graphVocab.txt");
        FileUtil.save2dListFile(graphReprensentList, fileFolder + "graphReprensent.txt");
        FileUtil.saveListFile(traceList, fileFolder + "trace.txt");
        FileUtil.saveListFile(singlePredictionList, fileFolder + "singlePrediction.txt");
        FileUtil.saveListFile(blockPredictionList, fileFolder + "blockPrediction.txt");
        FileUtil.saveListFile(holeSizeList, fileFolder + "holeSize.txt");
    }
}
