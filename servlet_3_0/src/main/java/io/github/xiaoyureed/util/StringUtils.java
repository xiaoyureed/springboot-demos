/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed.util;

/**
 * @auther: xiaoyu
 * @date: 2018/10/28 23:11
 * @description:
 */
public final class StringUtils {
    private StringUtils() {
    }

    /**
     * (strict evaluation)
     *
     * @param str string to evaluate
     * @return blank or not
     */
    public static boolean isBlank(String str) {
        if (null == str) {
            return true;
        }
        if (str.length() == 0) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
