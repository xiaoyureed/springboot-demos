package io.github.xiaoyu.groovydemo.util;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * @author xiaoyu
 * date: 2020/3/16
 */
public class GroovyUtil {

    public static GroovyObject groovyObject(String classpathSource) throws URISyntaxException, IOException, IllegalAccessException, InstantiationException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(
                GroovyUtil.class.getClassLoader());
        Class groovyClass = groovyClassLoader.parseClass(Paths.get(Thread.currentThread()
                .getContextClassLoader().getResource(classpathSource).toURI()).toFile());
        return (GroovyObject) groovyClass.newInstance();
    }

}
