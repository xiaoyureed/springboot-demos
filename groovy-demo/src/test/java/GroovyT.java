import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.github.xiaoyu.groovydemo.util.GroovyUtil;
import io.github.xiaoyu.groovydemo.util.PathUtil;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author xiaoyu
 * date: 2020/3/16
 */
public class GroovyT {

    @Test
    public void t1() throws IOException, InstantiationException, IllegalAccessException, URISyntaxException {
        GroovyObject groovyObject = GroovyUtil.groovyObject("groovy/clazz/Cat.groovy");
        groovyObject.setProperty("id", UUID.randomUUID().toString());
        groovyObject.setProperty("name", "xiao");
        System.out.println(groovyObject);
        groovyObject.invokeMethod("hi", null);
    }

    @Test
    public void t2() throws IOException, ResourceException, ScriptException {
        // 若是数组, 则有多个脚本地址
        // GroovyScriptEngine 可以作为一个全局唯一的 static 成员, 不必每次都创建
        GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(PathUtil.absoluteClasspath("groovy/script/"));
        Binding binding = new Binding();
        binding.setProperty("input", "xiao");
        groovyScriptEngine.run("demo.groovy", binding);
    }

    @Test
    public void t2_1() throws javax.script.ScriptException, FileNotFoundException, NoSuchMethodException {
        // GroovyScriptEngineFactory 可以作为 静态成员, 不必每次都创建
        GroovyScriptEngineFactory groovyScriptEngineFactory = new GroovyScriptEngineFactory();
        ScriptEngine              scriptEngine              = groovyScriptEngineFactory.getScriptEngine();
        scriptEngine.eval(new FileReader(Paths.get(PathUtil.absoluteClasspath("groovy/script/demo1.groovy")).toFile()));
        Invocable invocable = (Invocable) scriptEngine;
        invocable.invokeFunction("func", "xiao");
    }
}
