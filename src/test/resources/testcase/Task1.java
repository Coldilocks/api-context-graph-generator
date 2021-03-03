package Experiment;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class Task1{

    public void test(){
        Map<String, List<String>> map = new HashMap<>();
        map.computeIfAbsent("new key", v->new ArrayList<>()).add("new value");
    }

//    public void test1(){
//        FileWriter fileWriter = new FileWriter("");
//        String str = "";
//        String str5;
//        File file2;
//
//        fileWriter.close();
//
//
//        str.concat(str.toUpperCase());
//
//        str.replace(" ", "").toUpperCase();
//
//
//        System.out.println("hello world");
//
//
////        String str1;
////        String str2 = "hello world";
////        String str3 = null;
////        File file = new File("xxxx");
////
//        StringBuilder stringBuilder = new StringBuilder();
////        String str4 = stringBuilder.toString();
//
//
//
//
//        str5 = "hello world";
//        str5 = null;
//        file2 = new File("xxxx");
//        str5 = stringBuilder.toString();
//
//
//    }

//    private File file;
//
//    public void firstMethod(String path) throws FileNotFoundException {
////        File file = new File(path);
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
//        String test6 = test2.toLowerCase().toUpperCase();
//
//        String test7 = Integer.valueOf(test3).toString();
//
//        String test8 = test1.substring(test4.indexOf(" "));
//
//
//    }

//    public void secondMethod(){
//        int a = 0;
//
//        switch (a){
//            case 0:
//                System.out.println(a);
//                break;
//            case 1:
//                System.out.println("hello world".toString());
//                break;
//            default:
//                System.out.println("hello world".toLowerCase());
//        }
//
//        int b = 0;
//    }

//    public void thirdMethod(){
//        //        StringBuilder str = new StringBuilder("text".toString());
////        System.out.println("hello world");
//
////        int[][] test = new int[3][4];
////        int index = 0;
////        int a = test[index];
//
////        for(char x : "hello"){
////            System.out.println(c);
////        }
//
//
////         List<String> list = new ArrayList<String>();
////         int i = 10, j =10;
//
//
//        // test case 1:
////        String test;
////        String str1 = "hello world";
////        test = str1.replace("test", "").toString();
////
////        if(!str1.isEmpty()){
////            test = "!";
////            test.toString();
////            String testForScopeVar = "hello";
////            testForScopeVar.toLowerCase();
////        }
////        String testForScopeVar = "world";
////        testForScopeVar.toString();
////
////        str1 = test.replace("test", "").toString();
//
//
//
//        // test case 2:
////        String str1 = new String("hello world");
////        String temp1 = "test";
////        String temp2 = "test";
////        String str2 = str1.concat(temp1.toString()).replaceAll(temp1.toUpperCase(), temp2);
////        str2.length();
//
//
//        // test case 3:
////        File file = new File(path);
//
//        // test case 4: conditional expression
////        String xx = "test";
////        String x = xx.isEmpty() ? xx.toLowerCase() : xx.toUpperCase();
//
//
////        this.file = new File(path);
//    }



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

}
