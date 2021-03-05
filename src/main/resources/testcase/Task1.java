package Experiment;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task1{
    public void readContentFromFile(String path) throws FileNotFoundException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        do{
            file.mkdir();
            StringBuilder stringBuilder1 = new StringBuilder();
            InputStream inputStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            DataInputStream dataInputStream = new DataInputStream(inputStream);
        }while (file.length() > 500);
        if(!file.exists()){
            try{
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = "";
                while((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                    stringBuilder.append("\r\n");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                stringBuilder.append("this is a test.");
            }
        }else if(file.length() > 50){
            File file1 = new File("sada");
            FileReader fileReader = new FileReader(file1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        }else {
            file.delete();
        }

        return stringBuilder.toString();
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

    public static String replaceName(String originStr){
        Pattern P = Pattern.compile("([A-Za-z][A-Za-z0-9]*\\.)+([A-Z][A-Za-z0-9]*\\()");

        Matcher m = P.matcher(originStr);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String matchedStr = m.group();
            String needReplaceStr = m.group(2);
            String prefixStr = matchedStr.substring(0, matchedStr.length() - needReplaceStr.length());
            m.appendReplacement(sb, prefixStr + "new(");
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
