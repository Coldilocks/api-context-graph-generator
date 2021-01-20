package util;

import java.util.UUID;

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
}
