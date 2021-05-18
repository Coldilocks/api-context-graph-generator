package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void saveStringInFile(String str, FileWriter writer){
        try{
            writer.write(str + "\r\n");
            writer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void save2dListFileWithoutClose(List<List<String>> data, FileWriter writer){
        try{
            for(List<String> list : data){
//                writer.write(list.toString() + "\r\n");
                writer.write(list.toString().replaceAll(" ", "") + "\r\n");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveListFileWithoutClose(List<String> data, FileWriter writer){
        try{
            for(String str : data){
                writer.write(str + "\r\n");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    /**
     * 获取一个目录下所有项目的绝对路径
     * @param filePath
     * @return
     */
    public static List<String> getProjectRootPathList(String filePath) {
        List<String> projectRootPathList = new ArrayList<>();

        File file = new File(filePath);
        File[] fs = file.listFiles();
        for(File f: fs) {
            if(f.isDirectory() && f.listFiles()!=null) {
                projectRootPathList.add(f.getPath());
            }
        }

        return projectRootPathList;
    }

    /**
     * 获取一个文件夹中所有的java文件
     * @param file 根目录
     * @param files 存储java文件路径的list
     */
    public static void getAllJavaFileList(File file, List<String> files) {
        if(file.isDirectory() && file.listFiles() != null){
            File[] fs = file.listFiles();
            for (File f : fs){
                getAllJavaFileList(f, files);
            }
        } else if(file.isFile() && file.getPath().endsWith(".java")){
            files.add(file.getPath());
        }
    }

    /**
     * 获取一个文件夹中所有的jar文件
     * @param file 根目录
     * @param files 存储jar文件路径的list
     */
    public static void getAllJarFileList(File file, List<String> files) {
        if(file.isDirectory() && file.listFiles() != null){
            File[] fs = file.listFiles();
            for (File f : fs){
                getAllJarFileList(f, files);
            }
        } else if(file.isFile() && file.getPath().endsWith(".jar")){
            files.add(file.getPath());
        }
    }

    /**
     * 获取数据集中所有Java文件的路径
     * @param projectsDirectory 原始数据集的根目录
     * @param outputPath 存储java文件路径列表的txt文件路径
     */
    public static void getAllJavaFileList(String projectsDirectory, String outputPath) {
        List<String> allJavaFilePathList = new ArrayList<>();
        getAllJavaFileList(new File(projectsDirectory), allJavaFilePathList);
        saveListFile(allJavaFilePathList, outputPath);
    }

    public static void getAllJarFileList(String jarFileFolder, String outputPath) {
        List<String> allJarFilePathList = new ArrayList<>();
        getAllJarFileList(new File(jarFileFolder), allJarFilePathList);
        saveListFile(allJarFilePathList, outputPath);
    }

    public static void main(String[] args) {
        getAllJarFileList("/Users/coldilock/Documents/Code/Github/CodeRecPro/src/main/resources/input/jarfiles",
                "/Users/coldilock/Documents/Code/Github/CodeRecPro/src/main/resources/input/jar_file_list.txt");
    }


}
