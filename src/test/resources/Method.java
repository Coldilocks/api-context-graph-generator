package test;

//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.lang.*;

/**
 * @author coldilock
 */
public class Method {

    private String filePath = "";

    public static String getContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    private String filePath2 = "";

//    public String test1(String []strs, String connector){
//        if(strs == null || strs.length == 0){
//            return "";
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for(String s : strs){
//            sb.append(s);
//            sb.append(connector);
//        }
//        return sb.substring(0, sb.length() - connector.length());
//    }


//    public String test2(String []strs, String connector){
//        if(!ArrayUtils.isEmpty(strs)){
//            StringBuilder sb = new StringBuilder();
//            for(String s : strs){
//                sb.append(s);
//                sb.append(connector);
//            }
//            return sb.substring(0, sb.length() - connector.length());
//        }
//        return "";
//    }



//    public String test3(String []strs, String connector){
//        if(strs == null || strs.length == 0){
//            return "";
//        }
//        StringBuilder sb = new StringBuilder();
//        for(String s : strs){
//            sb.append(connector + s);
//        }
//        return sb.substring(connector.length(), sb.length());
//    }

    //    public String test4(String []strs, String connector){
//        if(strs != null && strs.length != 0){
//            StringBuilder sb = new StringBuilder();
//            for(String s : strs){
//                sb.append(connector).append(s);
//            }
//            return sb.substring(connector.length(), sb.length());
//        }
//        return "";
//    }

    //    public String test5(String []strs, String connector){
//        if(ArrayUtils.isEmpty(strs)){
//            return StringUtils.EMPTY;
//        }
//        boolean sign = true;
//        StringBuilder sb = new StringBuilder();
//        for(String s : strs){
//            if(sign){
//                sb.append(connector);
//            } else {
//                sign = true;
//            }
//            sb.append(s);
//        }
//        return sb.toString();
//    }


    //     public String test6(String []strs, String connector){
    //     if(ArrayUtils.isEmpty(strs)){
    //         return StringUtils.EMPTY;
    //     }
    //     boolean sign = true;
    //     StringBuilder sb = new StringBuilder();
    //     for(String s : strs){
    //         if(!sign){
    //             sb.append(connector);
    //         }
    //         sign = false;
    //         sb.append(s);
    //     }
    //     return sb.toString();
    // }


//    public String test7(String []strs, String connector){
//        if(ArrayUtils.isEmpty(strs)){
//            return StringUtils.EMPTY;
//        }
//        boolean sign = false;
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < strs.length; i++){
//            if(sign){
//                sb.append(connector);
//            }
//            sign = true;
//            sb.append(strs[i]);
//        }
//        return sb.toString();
//    }

    //    public String test8(String []strs, String connector){
//        if(ArrayUtils.isEmpty(strs)){
//            return StringUtils.EMPTY;
//        }
//        boolean sign = true;
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < strs.length; i++){
//            if(!sign){
//                sb.append(connector);
//            } else {
//                sign = false;
//            }
//            sb.append(strs[i]);
//        }
//        return sb.toString();
//    }
}