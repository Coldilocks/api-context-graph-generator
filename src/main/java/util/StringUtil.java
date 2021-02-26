package util;

import edu.stanford.nlp.simple.Sentence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author coldilock
 */
public class StringUtil {

    public static String getUuid(){
//        return UUID.randomUUID().toString().replaceAll("-", "");
        Integer orderId = UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return String.valueOf(orderId);
    }

    /**
     * replace the second className to "new"
     * @param originStr e.g. "java.lang.String.String()"
     * @return e.g. "java.lang.String.new()"
     */
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

    /**
     * 移除变量名中的数字和方括号
     * @param str
     * @return
     */
    public static String removeNumAndParentheses(String str){
        return str.replaceAll("\\d|\\[]", "_");
    }

    /**
     * 分割用'_'或'$'符号命名的变量
     * @param string
     * @return
     */
    public static List<String> splitSpecialCharacter(String string){
        List<String> list = new ArrayList<>();
        String[] result = string.split("_|\\$");
        for (String s : result) {
            if (!s.isEmpty()) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 对使用驼峰命名法命名的变量进行分割
     * @param input
     * @return
     */
    public static String splitCamel(String input){
        List<String> tempList = new ArrayList<>();
        for (String w : input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            tempList.add(w.toLowerCase());
        }
        return String.join("_", tempList);
    }

    /**
     * 词形还原
     * @param word
     * @return
     */
    public static String getLemma(String word) {
        Sentence sentence = new Sentence(word);
        if(sentence.lemmas().size() > 0){
            return sentence.lemmas().get(0);
        } else {
            return word;
        }
    }


    public static List<String> getSplitVariableNameList(String variableName, List<String> gloveVocabList, List<String> stopWordsList) {

        List<String> finalList = new ArrayList<>();

        // 替换数字及[]
        String variableWithoutNumberAndParentheses = removeNumAndParentheses(variableName);

        // 按'_'和'$'进行split
        List<String> originalList = splitSpecialCharacter(variableWithoutNumberAndParentheses);

        // 按照Camel case进行分词(得到的词用"_"连接，还要再按照"_"进行一次split)
        List<String> camelCaseSplitList = new ArrayList<>();
        for (String s : originalList) {
            String camelCaseSplitString = splitCamel(s);
            camelCaseSplitList.add(camelCaseSplitString);
        }

        // 对进行了camel case split后的单词按照'_'进行split
        List<String> camelCaseFinalList = new ArrayList<>();
        for (String s : camelCaseSplitList) {
            List<String> tempList = splitSpecialCharacter(s);
            camelCaseFinalList.addAll(tempList);
        }

        // Stemming
        List<String> stemmingList = new ArrayList<>();
        for (String s : camelCaseFinalList) {
            stemmingList.add(getLemma(s));
        }

        // stop words
        List<String> filterStopWordsList = new ArrayList<>();
        for (String s : stemmingList) {
            if (!stopWordsList.contains(s) && s.length() > 1) {
                filterStopWordsList.add(s);
            }
        }

        // Glove检查
        for (String s : filterStopWordsList) {
            if (gloveVocabList.contains(s)) {
                finalList.add(s);
            }
        }

        return finalList;
    }
}
