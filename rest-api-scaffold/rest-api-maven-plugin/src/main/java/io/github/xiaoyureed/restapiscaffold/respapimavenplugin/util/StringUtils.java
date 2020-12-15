package io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util;

/**
 * @author xiaoyu
 * @date 2019/5/14
 */
public final class StringUtils {
    private StringUtils(){}

    /**
     * 检查是否是有效字符串
     */
    public static boolean isValid(String target) {
        return target != null && target.trim().length() > 0;
    }

    public static boolean isNotValid(String target) {
        return !isValid(target);
    }

    /**
     * 首字母大写
     */
    public static String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 转为驼峰命名格式
     */
    public static String convertToCamel(String source) {
        if (!source.contains("_")) {
            return source;
        }
        final String[]      split  = source.split("_");
        final StringBuilder target = new StringBuilder(split[0]);
        for (int i = 1; i <split.length; i++) {
            target.append(uppercaseFirstLetter(split[i]));
        }
        return target.toString();
    }

    /**
     * 从 full class name 截取 short class name
     */
    public static String getNameFromFullName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }
}