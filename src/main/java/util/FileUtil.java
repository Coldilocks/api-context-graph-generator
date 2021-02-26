package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coldilock
 */
public class FileUtil {

    public static List<String> gloveVocabList;
    public static List<String> stopWordsList;

    public static void save2dListFile(List<List<String>> data, String filePath){
        try{
            File file = new File(filePath);

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for(List<String> list : data){
                bw.write(list.toString() + "\r\n");
            }
            bw.flush();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveListFile(List<String> data, String filePath){
        try{
            File file = new File(filePath);

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for(String str : data){
                bw.write(str + "\r\n");
            }

            bw.flush();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取txt文件
     * @param fileName 文件路径
     * @return list，每一行的内容是一个元素
     * @throws Exception
     */
    public static List<String> readFile2List(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        Files.lines(Paths.get(fileName)).forEach(line -> list.add(line.trim()));
        return list;
    }

    /**
     * 加载 gloveVocabList 和 stopWordsList
     * @throws Exception
     */
    public static void initVocab() throws Exception {
        gloveVocabList = FileUtil.readFile2List(DataConfig.GLOVE_VOCAB_PATH);
        stopWordsList = FileUtil.readFile2List(DataConfig.STOP_WORDS_PATH);
    }
}
