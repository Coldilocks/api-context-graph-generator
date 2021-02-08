package util;

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
}
