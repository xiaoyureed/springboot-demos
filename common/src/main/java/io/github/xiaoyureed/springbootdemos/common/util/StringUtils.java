package io.github.xiaoyureed.springbootdemos.common.util;

/**
 * @author xiaoyu
 * date: 2020/3/24
 */
public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isStrictEmpty(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isStrictNotEmpty(String str) {
        return !isStrictEmpty(str);
    }
}



