package io.github.xiaoyu.groovydemo.util;

/**
 * @author xiaoyu
 * date: 2020/3/16
 */
public class PathUtil {
    public static String absoluteClasspath(String classpath) {
        return Thread.currentThread().getContextClassLoader()
                .getResource(classpath).getPath();
    }

}
