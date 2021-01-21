package Experiment;

import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.util.ArrayList;

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

    public void readContentFromFile(String path) throws FileNotFoundException {
//        StringBuilder str = new StringBuilder("text".toString());
//        System.out.println("hello world");
//
        int[][] test = new int[3][4];
        int index = 0;
        int a = test[index];

//        for(char x : "hello"){
//            System.out.println(c);
//        }


         List<String> list = new ArrayList<String>();
         int i = 10, j =10;


        // test case 1:
//        String test;
//        String str1 = "hello world";
//        test = str1.replace("test", "").toString();
//
//        if(!str1.isEmpty()){
//            test = "!";
//            test.toString();
//            String testForScopeVar = "hello";
//            testForScopeVar.toLowerCase();
//        }
//        String testForScopeVar = "world";
//        testForScopeVar.toString();
//
//        str1 = test.replace("test", "").toString();



        // test case 2:
//        String str1 = new String("hello world");
//        String temp1 = "test";
//        String temp2 = "test";
//        String str2 = str1.concat(temp1.toString()).replaceAll(temp1.toUpperCase(), temp2);
//        str2.length();



        // test case 3:
//        File file = new File(path);
//        StringBuilder stringBuilder = new StringBuilder();
//        do{
//            file.mkdir();
//            StringBuilder stringBuilder1 = new StringBuilder();
//            InputStream inputStream = new InputStream() {
//                @Override
//                public int read() throws IOException {
//                    return 0;
//                }
//            };
//            DataInputStream dataInputStream = new DataInputStream(inputStream);
//        }while (file.length() > 500);
//        if(!file.exists()){
//            try{
//                FileReader fileReader = new FileReader(file);
//                BufferedReader bufferedReader = new BufferedReader(fileReader);
//                String line = "";
//                while((line = bufferedReader.readLine())!=null){
//                    stringBuilder.append(line);
//                    stringBuilder.append("\r\n");
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                stringBuilder.append("this is a test.");
//            }
//        }else if(file.length() > 50){
//            File file1 = new File("sada");
//            FileReader fileReader = new FileReader(file1);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//        }else {
//            file.delete();
//        }
//
//        return stringBuilder.toString();
    }

}
