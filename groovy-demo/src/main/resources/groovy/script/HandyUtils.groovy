package groovy.script

/**
 * @author xiaoyu* @date 2019/5/25
 */
//class HandyUtils {
//}

/**
 * ConfigSlurper 用来处理 groovy script 格式的 config file
 */

void func1() {
    def config = new ConfigSlurper().parse('''
        app.date = new Date()
        app.age = 10
        app {
            name = "Test${10}"
        }
        app."a.b" = "a.b hello"
    ''')

    assert config.app.date instanceof Date
    assert config.app.age == 10
    assert config.app.name == 'Test10'
    assert config.app.xxx != null // // 要么返回配置值，要么返回一个新的 ConfigObject 实例，但永远不会返回 null 值
    assert config.app."a.b" == "a.b hello" // 单引号亦可
}

// 设置多环境, 简单起见, `environments` 不变
void func2() {

    def config = new ConfigSlurper('dev').parse('''
        environments {
            dev {
                app.port = 8080
            }
            prod {
                app.post = 80
            }
            test {
                app.port = 8081
            }
        }
    ''')
    assert config.app.port == 8080
}

//`environments` 可以修改, 但是对应的也需要注册操作
void func3() {
    def slurper = new ConfigSlurper()
    slurper.registerConditionalBlock('myProject', 'developers')

    def config = slurper.parse('''
        sendMail = true
        
        myProject {
           developers {
               sendMail = false
           }
        }
    ''')

    assert config.sendMail == false
}

// 和 Java 的 properties 文件整合
void func4() {
    def config = new ConfigSlurper().parse('''
        app.date = new Date()
        app.age  = 42
        app {
            name = "Test${42}"
        }
        haha = "haha ${app.date}"
    ''')

    def properties = config.toProperties()

    println(properties."app.date") // Sat May 25 17:49:02 CST 2019
    println(properties."haha") // haha Sat May 25 17:50:44 CST 2019

    assert properties."app.date" instanceof String
    assert properties."app.age" == '42'
    assert properties."app.name" == 'Test42'
}

//func1()
//func2()
//func3()
//func4()


/**
 * Expando - 动态可拓展对象
 */

void exp1() {
    def expando = new Expando()
    expando.name = 'name'
    expando.someMethod = {s -> "some method. parameter = ${s}"}

    println(expando.name)
    println(expando.someMethod('aa'))

    assert expando.name == 'name'
    assert expando.someMethod('aa') == 'some method. parameter = aa'
}

//exp1()

/**
 * Observable list, map and set 可观测集合, 列表
 */
