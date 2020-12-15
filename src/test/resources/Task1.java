package Experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Task1{

    public String readContentFromFile(String path) throws FileNotFoundException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        if(file.exists()){
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

}
