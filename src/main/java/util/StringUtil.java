package util;

import java.util.UUID;

/**
 * @author coldilock
 */
public class StringUtil {

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
