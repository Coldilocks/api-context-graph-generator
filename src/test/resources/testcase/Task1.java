package Experiment;

import java.io.*;

public class Task1{

//    public void test(String path) throws FileNotFoundException {
//        File file = new File(path);
//        file.mkdir();
//
//        String test1 = "hello world";
//
//        String test2 = null;
//
//        String test3;
//        test3 = null;
//
//        String test4 = new String("hello world");
//
//        String test5 = test1.toLowerCase();
//
//        String test6 = test1.toLowerCase().toUpperCase();
//
//        String test7 = Integer.valueOf(test1).toString();
//
//        String test8 = test1.substring(test1.indexOf(" "));
//
//
//    }

    public String readContentFromFile(String path) throws FileNotFoundException {
        String str = "hello";
        if(str != null && str.length() > 0)
            System.out.println("hello world");


        // StringBuilder str = new StringBuilder("text".toString());
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

}
