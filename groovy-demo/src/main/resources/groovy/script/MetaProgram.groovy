package groovy.script

import groovy.time.TimeCategory

/**
 * MOP
 * @author xiaoyu* @date 2019/5/25
 */
//class MetaProgram {
//}

/**
 * 运行时元编程
 * POJO - 普通 Java对象
 * POGO - groovy 对象, 继承自 java.lang.Object 且默认实现了 groovy.lang.GroovyObject 接口。
 * Groovy interceptor - Groovy 拦截器 —— 实现了 groovy.lang.GroovyInterceptable 接口的 Groovy 对象，并具有方法拦截功能
 */

/**
 * GroovyObject 接口:
 *
 * public interface GroovyObject {
 *
 *      // 除了具有 methodMissing的功能之外, 和 GroovyInterceptable 配合, 可以拦截所有方法, 开销大
 *      // 如果希望更通用的方式: 在一个对象的 MetaClass 上实现 invokeMethod()。该方法同时适于 POGO 与 POJO 对象
 *     Object invokeMethod(String methodName, Object methodArgs);
 *
 *      // 每次读取 pogo 对象的field(无论是否存在)时, 都先调用这个方法
 *     Object getProperty(String propertyName);
 *
 *      // 每次设置properties时调用
 *     void setProperty(String propertyName, Object newValue);
 *
 *     MetaClass getMetaClass();
 *
 *     void setMetaClass(MetaClass metaClass);
 * }
 */

/**
 * methodMissing - 拦截缺失的方法调用 (https://www.cnblogs.com/maijunjin/articles/3043935.html)
 *                  使用 methodMissing，并不会产生像调用 invokeMethod 那么大的开销，第二次调用代价也并不昂贵
 *
 * propertyMissing  - 拦截缺失的属性调用
 */

class InvokeMethodDemo1 {
    // override invokeMethod()
    def invokeMethod(String method, Object args) {
        "method ${method}(${args.join(', ')}) missing"
    }
    def test() {
        'method test exist'
    }
}

void func1() {
    def demo = new InvokeMethodDemo1()
    assert demo.test() == 'method test exist'
    assert demo.xxx() == 'method xxx() missing'
    assert demo.yyy('abc') == 'method yyy(abc) missing'
}

//func1()

class InvokeMethodDemo2 implements GroovyInterceptable {
    def invokeMethod(String method, Object args) {
        'hoho'
    }
    def test1() {
        println 'test1'
    }
}

void func2() {
    def demo = new InvokeMethodDemo2()
    assert demo.test1() == 'hoho'
    assert demo.xxx() == 'hoho' // 调用不存在的方法, 也被拦截

    // 如果想要拦截所有的方法调用，但又不想实现 GroovyInterceptable 这个接口，那
    // 么可以在一个对象的 MetaClass 上实现 invokeMethod()
    // 同时适于 POGO 与 POJO 对象
    def str = 'aa'
    str.metaClass.invokeMethod = {String method, Object args ->
        "invoke method ${method}"
    }
    assert str.length() == 'invoke method length'
    assert str.xxx() == 'invoke method xxx'
}
//func2()


class GetPropertyDemo {
    def f1 = 'aa'
    def f2 = 'bb'

    def getProperty(String field) {
        if (field == 'f3') { // 拦截对 f3 的读取
            return 'f3'
        }
        // 其他的 field 放行
        return metaClass.getProperty(this, field)
    }

    def getF4() {
        'f4'
    }

    void setProperty(String name, Object value) {
        this.@"$name" = value + '-tail'
    }
}

void testGetPropertyDemo() {
    def demo = new GetPropertyDemo()

    assert demo.f1 == 'aa'
    assert demo.f2 == 'bb'

    assert demo.f3 == 'f3'
    assert demo.f4 == 'f4'
}
//testGetPropertyDemo()

class GetSetAttrDemo{
    def f1 = 'f1'
    def f2 = 'f2'

    def f3
    def f4

}
void testGetSetAttrDemo() {
    def demo = new GetSetAttrDemo()

    assert demo.metaClass.getAttribute(demo, 'f1') == 'f1'
    assert demo.metaClass.getAttribute(demo, 'f2') == 'f2'

    demo.metaClass.setAttribute(demo, 'f3', 'f3')
    demo.metaClass.setAttribute(demo, 'f4', 'f4')
    assert demo.metaClass.getAttribute(demo, 'f3') == 'f3'
    assert demo.metaClass.getAttribute(demo, 'f4') == 'f4'
}
//testGetSetAttrDemo()

class MethodMissingDemo {
    def methodMissing(String name, def args) {
        return "this is me"
    }

    // 针对静态方法调用的缺失
    static def $static_methodMissing(String name, Object args) {
        return "Missing static method name is $name"
    }
}
void testMethodMissingDemo() {
    def demo = new MethodMissingDemo()
    assert demo.someMethod(20) == 'this is me'
    assert MethodMissingDemo.xxx() == 'Missing static method name is xxx'
}
//testMethodMissingDemo()

class PropertyMissingDemo1 {
    def propertyMissing(String f) { f }

    // 针对静态field调用的缺失
    static def $static_propertyMissing(String name) {
        return "Missing static property name is $name"
    }
}
void testPropertyMissingDemo1() {
    assert new PropertyMissingDemo1().aaa == 'aaa'
    assert PropertyMissingDemo1.xx == 'Missing static property name is xx'

}
//testPropertyMissingDemo1()

// 实现动态添加 field
class PropertyMissingDemo2 {
    def storage = [:] // map

    // 针对 setter
    def propertyMissing(String name, value) { storage[name] = value }

    // 针对 getter
    def propertyMissing(String name) { storage[name] }
}

void testPropertyMissingDemo2() {
    def demo2 = new PropertyMissingDemo2()
    demo2.xxx = 'aaa'

    assert demo2.xxx == 'aaa'
}
//testPropertyMissingDemo2()


/**
 * Groovy 从 Objective-C 那里借用并实现了一个概念，叫做：类别（Categories）
 *
 * groovy 默认提供几个 categories class: TimeCategory, ServletCategory, DOMCategory
 *
 *          categories class 中的方法均为static的
 *
 * 类别类默认是不能启用的。要想使用定义在类别类中的方法，必须要使用 GDK 所提供的 use(CategoriesClass, Closure)
 */
void testCategories() {
    use(TimeCategory, {// closure 可以写到 小括号之外
        println 1.minute.from.now // 一分钟之后 // TimeCategory 为 Integer 添加了方法
        println(10.hour.ago)// 十小时之前

        def date = new Date()
        println(date - 3.months)// 三个月之前 // TimeCategory 为 Date 添加了方法
    })
}

//testCategories()

/**
 * 编译时元编程
 */





