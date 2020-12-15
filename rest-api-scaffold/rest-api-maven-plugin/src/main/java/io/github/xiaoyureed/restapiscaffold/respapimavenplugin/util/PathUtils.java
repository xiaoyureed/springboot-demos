package io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.GlobalConfig;

import java.io.File;

/**
 * @author xiaoyu
 * @date 2019/5/14
 */
public final class PathUtils {
    private PathUtils() {
    }

    // C:\Users\xiaoyu\iCloudDrive\repo\rest-api-scaffold\demo-mvn
    private static final String ROOT_DIR = System.getProperty("user.dir");

    private static final GlobalConfig CONFIG = GlobalConfig.me();

    private static final String SEPARATOR = File.separator;

    /**
     * source code Directory
     */
    private static final String CODE_DIR     = SEPARATOR + "src"+ SEPARATOR +"main"+ SEPARATOR +"java";
    /**
     * resources Directory
     */
    private static final String RESOURCE_DIR = SEPARATOR + "src"+ SEPARATOR +"main"+ SEPARATOR +"resources";

    public static String commonAopPath() {
        return commonPath() + "aop" + SEPARATOR;
    }

    public static String commonConfigPath() {
        return commonPath()  + "config" + SEPARATOR;
    }

    public static String commonDaoPath() {
        return commonPath() + "dao" + SEPARATOR;
    }

    public static String commonDaoImplMybatis() {
        return commonDaoPath() + "impl" + SEPARATOR + "mybatis" + SEPARATOR;
    }

    public static String commonExceptionPath() {
        return commonPath() + "exception" + SEPARATOR;
    }

    public  static String commonInterceptor() {
        return commonPath() + "interceptor" + SEPARATOR;
    }

    public static String commonUtil() {
        return commonPath() + "util" + SEPARATOR;
    }

    public static String commonPath() {
        return packagePath() + SEPARATOR + "common" + SEPARATOR;
    }

    /**
     * build controller's package path
     */
    public static String controllerPath() {
        return packagePath() + SEPARATOR + "controller" + SEPARATOR;
    }

    /**
     * build DTO's package path
     */
    public static String pojoDtoPath() {
        return packagePath() + SEPARATOR + "pojo" + SEPARATOR + "dto" + SEPARATOR;
    }

    /**
     * build SQL DTO's package path
     */
    public static String sqlDtoPath() {
        return packagePath() + SEPARATOR + "sql" + SEPARATOR + "dto" + SEPARATOR;
    }

    /**
     * build service's package path
     */
    public static String servicePath() {
        return packagePath() + SEPARATOR + "service" + SEPARATOR;
    }

    public static String mapperPath() {
        return resourcePath() + SEPARATOR + "mapper" + SEPARATOR;
    }

    /**
     * resources 目录
     */
    private static String resourcePath() {
        return ROOT_DIR + RESOURCE_DIR;
    }

    /**
     * Java code 所在目录
     */
    private static String packagePath() {
        String basePackage = CONFIG.basePackage;
        if (basePackage.contains(".")) {
            basePackage = basePackage.replaceAll("\\.", SEPARATOR);
        }
        return ROOT_DIR + CODE_DIR + SEPARATOR + basePackage;
    }

}