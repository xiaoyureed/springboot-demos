package groovy.script

import groovy.transform.PackageScope
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * @author xiaoyu* @date 2019/5/25
 */
//class DifferentWithJava {
//}

/**
 * - default imports
 */

/////////////////

/**
 * multi methods (方法重载) ------ dynamic binding
 */

int method(String str) {
    return 1
}

int method(Object obj) {
    return 2
}

Object o = "xxx"
def result = method(o)
println result // 1

/////////////////

/**
 * boxing/unboxing vs. widening ----- 在Java中 widening 优先于 boxing
 *
 * 但是在groovy 中, all primitive references use their wrapper class (基础类型自动使用包装对象)
 */

int i
m(i) // 下面那个方法会调用?

void m(long l) { // java 中会调用, 在Java中 widening(延展) 优先于 boxing
    println "in m(long)"
}

void m(Integer i) {// groovy 中会调用
    println "in m(Integer)"
}

/**
 * - array init -------- 使用 `[...]`, `{...}` 留给了closure
 */

int[] array = [1, 2, 3]

//////////////

/**
 * - package scope ------ @PackageScope 包私有字段
 */
class Person implements Serializable {
    @PackageScope
    String name // 使用 @PackageScope 标注, 那么这个field是 package-private field

    int age // 并不是 package-private field, 而是一个 private field, 以及 getter , setter

    Person(name, age) {
        this.name = name
        this.age = age
    }
}

/**
 * class Pro {
 *     Integer property1 //1 没用private public protected(gorrvy里面是            @PackageScope)
 *     def property2   //2 可选的statoc final(有final就会没有set) 之类的
 *     final String property3  //3 def 或者 确定的类型
 *     static String property4 //4 property名字
 *     //满足以上四个条件就是一个property
 *
 *     //实际上等于java以下内容,一个private+getter+setter(final修饰的没有setter)
 *     private String property5
 *
 *     String getProperty5() {*     return property5
 * }
 *
 * void setProperty5(String property5) {
 *     this.property5 = property5
 *     }
 * }
 * 
 * static void main(String[] args) {*
 *     def pro = new Pro()
 *     Pro pro1 = [:]//也可以这样声明,前提是必须指定类型,并且有默认的构造器,以下等价
 *     Pro pro2 = []//等价以上
 *     Pro pro3 = [property1: 123]//等价以上
 *     def pro4 = [property1: 123] as Pro//等价以上
 *     pro.property1 = 123 //类似pro.setProperty1(123)
 *     println(pro.property1)//类似pro.getProperty1()
 * }
 */

/////////////////////////

/**
 * ARM blocks (automatic resources management) ------- 类似Java中的 try () {}*/

// read file, demo1
new File("./package-info.java").eachLine("UTF-8") {
    println it
}

// read file, demo2 ---- 更像Java的版本
new File("package-info.java").withReader('UTF-8') { reader ->
    reader.eachLine {
        println it
    }
}

///////////////

/**
 * static inner class - 如果用到内部类, 一般就是静态内部类
 */
class A {
    static class B {}
}

new A.B() // 和 Java 习惯一样

/////////////////////

/**
 * non static inner class ----- 不支持 y.new X()，需要使用 new X(y)
 */

class Y {
    class X {}

    X foo() {
        return new X()
    }

    static X createX(Y y) {
        return new X(y)
    }
}

//////////////////

/**
 * anonymous inner class
 */

//def called = new CountDownLatch(1)
//def timer = new Timer()
//timer.schedule(new TimerTask() {
//    @Override
//    void run() {
//        called.countDown()
//    }
//}, 0)
//assert called.await(2, TimeUnit.SECONDS)

//////////////////

/**
 * lambda expression ---- java8 中的 lambda 实际上是 anonymous inner class, groovy 中的才是真正的lambda -- closure(闭包)
 */

Runnable run = {
    println 'run'
}
def list = Arrays.asList(1, 2, 3, 4)
list.each { println it }
list.each(this.&println)// 等价

/**
 * GString - groovy string ---- "abc${1}de", 允许 `${...}`
 */

assert 'c'.getClass() == String
assert "c".getClass() == String // 等价
assert "c${1}".getClass() in GString

char a='a'                  // 单个字符的 string 会自动转型为 char
assert Character.digit(a, 16)==10 : 'But Groovy does boxing'
assert Character.digit((char) 'a', 16)==10
println 'assert ok'

// for single char strings, both are the same
assert ((char) "c").class==Character
assert ("c" as char).class==Character // 等价

// 对于包含多个字符的字符串来说，两种模式的结果并不相同
try {
    ((char) 'cx') == 'c'
    assert false: 'will fail - not castable'
} catch(GroovyCastException e) {
    println '多个字符不能转为char'
}
assert ('cx' as char) == 'c' // Groovy 模式的转换更宽容一些，只取第一个字符
assert 'cx'.asType(char) == 'c'// 等价
println 'assert ok'

/**
 * "==" 的不同 --- 在 Java中, 表示对象完全相等(地址, 内容全相同), groovy 中表示 equals; 如果希望取得Java中的效果, 使用 a.is(b)
 */

/**
 * default 必须位于 swith / case 结构的结尾 --- 在 Java 中，它可以放在 swith / case 结构中的任何位置，但在 Groovy 中，它更像是一个 else 子句，而非一个默认的 case 子句
 */

int ab=1//不需要分号

String multiLine='''
1//适应osc的markdwon
2//适应osc的markdwon
3'''//多行字符串
String interpolationString="$map or ${map}"//这是插值字符串,基本现代的语言都有吧

/**
 * 集合
 */
def map=[a:1,b:2]//这是个map
def map2 = [:] //默认是LinkedHashMap
assert map2.class == LinkedHashMap
map.'a' = 1 // key 包含包含空白字符之类的可以用这种方式
map.b = 2
map << [c: 3]
// null 空集合(包含map) 0 空字符串 空数组 在boolean环境中是false
def aMap=[:]
if (aMap){//groovy的false值,上面也提过
    println('这段代码不会被执行')//aMap意味着false
}
map << ['c':3]//这是重载运算符,实现起来其实很简单

def list1=[1,2,3,4,5]//这是个list
def list2 = [] //默认是ArrayList
assert list2.class == ArrayList//可以省略.class

LinkedList list3 = []//因为默认是ArrayList,这种方式可以改变
assert list3.class == LinkedList

def numbers = [1,2,3,4]
assert numbers + 5 == [1,2,3,4,5]
assert numbers - [2,3] == [1,4] // 创建了新的集合实例


/**
 *
 * 范围: 例如 “0..4” 表明包含 整数 0、1、2、3、4。Groovy 还支持排除范围，“0..<4” 表示 0、1、2、3。还可以创建字符范围：“a..e” 相当于 a、b、c、d、e。“a..<e” 包括小于 e 的所有值
 *
 * for(i in 0..5) {}
 *
 * def range=1..5//这是个range
 */

/**
 * 默认参数值: def repeat(val, repeat=5) {}, 那么repeat 是可选的
 */



/**
 * 静态编译 @CompileStatic 标注在类上; 但是每一个类都加@CompileStatic实在太麻烦,不过groovy提供了一个特性.自定义CompilerConfiguration
 * 静态检查 @TypeChecked
 */

/**
 * magic method 魔法方法
 */
def numbers1 = [1,2,3,4]
assert numbers1.join(",") == "1,2,3,4"
assert [1,2,3,4,3].count(3) == 2

/**
 * 分布操作符 (spread operator) `*` -- 遍历集合
 */
assert ["JAVA", "GROOVY"] ==
        ["Java", "Groovy"]*.toUpperCase()

/**
 * 闭包 closure
 */
def acoll = ["Groovy", "Java", "Ruby"]

acoll.each{
    println it // it 为关键字, 默认, 代表每个元素,可以修改
}
// 等价
acoll.each{ value -> // 自定义项变量
    println value
}

// 怎么执行
def excite = { word ->
    return "${word}!!"
}
assert "Groovy!!" == excite("Groovy")
assert "Java!!" == excite.call("Java")// 等价, 不常用

/**
 * 对象的初始化: 默认提供构造函数, 默认提供 getter, setter -- def sng = new Song(name:"Le Freak", artist:"Chic", genre:"Disco")
 */

/**
 * 在 Groovy 中可以省略 return 语句。Groovy 默认返回方法的最后一行
 */

/**
 * 防止null 空指针异常 -- 在方法调用前面添加一个 ? 就相当于在调用前面放了一个条件，可以防止在 null 对象上调用方法。
 *
 * sng2.artist?.toUpperCase()
 */






