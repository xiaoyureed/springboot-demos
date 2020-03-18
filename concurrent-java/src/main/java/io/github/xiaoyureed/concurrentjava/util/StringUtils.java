package io.github.xiaoyureed.concurrentjava.util;

/**
 * @author xiaoyu
 * @since 1.0
 */
public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String test) {
        if (test == null) {
            return true;
        }
        if (test.length() == 0) {
            return true;
        }
        if (test.trim().length() == 0) {// stricter blank test
            return true;
        }
        return false;
    }

    //private boolean trimTest(String test) {
    //    if (test.trim().length() == 0) {
    //        return true;
    //    }
    //}
}
