package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author coldilock
 */
public class FileUtil {

//    public static void save2dListFile(String str, FileWriter writer){
//        try{
//            writer.write(str + "\r\n");
//            writer.flush();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

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

}
