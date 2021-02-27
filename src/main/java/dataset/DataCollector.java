package dataset;

import util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class DataCollector {

    /** file writers */
    public static FileWriter graphVocabWriter;
    public static FileWriter graphReprensentWriter;
    public static FileWriter traceWriter;
    public static FileWriter singlePredictionWriter;
    public static FileWriter blockPredictionWriter;
    public static FileWriter holeSizeWriter;
    public static FileWriter variableNameWriter;
    public static FileWriter originalStatementWriter;
    public static FileWriter methodNameWriter;

    /** data */
    public static List<List<String>> graphVocabList = new ArrayList<>();
    public static List<List<String>> graphReprensentList = new ArrayList<>();
    public static List<String> traceList = new ArrayList<>();
    public static List<String> singlePredictionList = new ArrayList<>();
    public static List<String> blockPredictionList = new ArrayList<>();
    public static List<String> holeSizeList = new ArrayList<>();
    public static List<String> variableNameList = new ArrayList<>();
    public static List<String> originalStatementList = new ArrayList<>();
    public static List<String> methodNameList = new ArrayList<>();

    public static void createWriters() throws IOException {
        String fileFolder = "/Users/coldilock/Downloads/output/";

        graphVocabWriter = new FileWriter(fileFolder + "graph_vocab.txt", true);
        graphReprensentWriter = new FileWriter(fileFolder + "graph_reprensent.txt", true);
        traceWriter = new FileWriter(fileFolder + "trace.txt", true);
        singlePredictionWriter = new FileWriter(fileFolder + "prediction.txt", true);
        blockPredictionWriter = new FileWriter(fileFolder + "block_prediction.txt", true);

        holeSizeWriter = new FileWriter(fileFolder + "hole_size.txt", true);
        variableNameWriter = new FileWriter(fileFolder + "variable_names.txt", true);
        originalStatementWriter = new FileWriter(fileFolder + "original_statement.txt", true);
        methodNameWriter = new FileWriter(fileFolder + "method_names.txt", true);

    }

    public static void saveCurrentData(){
        // 存储由当前方法构造的数据
        FileUtil.save2dListFileWithoutClose(graphVocabList, graphVocabWriter);
        FileUtil.save2dListFileWithoutClose(graphReprensentList, graphReprensentWriter);
        FileUtil.saveListFileWithoutClose(traceList, traceWriter);
        FileUtil.saveListFileWithoutClose(singlePredictionList, singlePredictionWriter);

        FileUtil.saveListFileWithoutClose(blockPredictionList, blockPredictionWriter);
        FileUtil.saveListFileWithoutClose(holeSizeList, holeSizeWriter);
        FileUtil.saveListFileWithoutClose(variableNameList, variableNameWriter);
        FileUtil.saveListFileWithoutClose(originalStatementList, originalStatementWriter);
        FileUtil.saveListFileWithoutClose(methodNameList, methodNameWriter);

        // 清空内容
        graphVocabList.clear();
        graphReprensentList.clear();
        traceList.clear();
        singlePredictionList.clear();
        blockPredictionList.clear();
        holeSizeList.clear();
        variableNameList.clear();
        originalStatementList.clear();
        methodNameList.clear();
    }

    public static void closeWriters() throws IOException {
        // close the writers
        graphVocabWriter.close();
        graphReprensentWriter.close();
        traceWriter.close();
        singlePredictionWriter.close();
        blockPredictionWriter.close();
        holeSizeWriter.close();
        variableNameWriter.close();
        originalStatementWriter.close();
        methodNameWriter.close();
    }

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
        FileUtil.saveListFile(methodNameList, fileFolder + "method_names.txt");
    }
}
