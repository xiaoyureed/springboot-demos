package groovy.script

import groovy.io.FileType
import groovy.script.Person

import java.nio.file.FileVisitResult

/**
 * @author xiaoyu* @date 2019/5/25
 */
//class WorkingWithIO {
//}

/**
 * reading file
 */

def file = new File("./package-info.java")

void func1(File file) {
    file.eachLine {line -> // 若 eachLine 中出现异常, resource 会自动关闭
        println line
    }
}

void func2(File file) {
    file.eachLine {line, ln ->
        println("Line $ln: " + line)
    }
}

List func3(File file) {
    return file.collect {it}
}

String[] func4(File file) {
    return file as String[]
}

byte[] func5(File file) {
    return file.bytes
}

void func6(File file) {
    file.withInputStream {inputStream -> // stream 会自动关闭
        new InputStreamReader(inputStream).eachLine {line ->
            println(line)
        }
    }
}

//func1(file)
//func2(file)
//func3(file).forEach(System.out.&println)
//Stream.of(func4(file)).forEach(System.out.&println)
//func6(file)


/**
 * write sth to a file
 */

void fun1(File file) {
    file.withWriter('utf-8') {writer ->
        writer.writeLine('// hello groovy')
    }
}

// 原样输出 '''xxx'''
void fun2(File file) {
    file << '''
// write sth new with `''`
'''
}

//fun1(file)
//fun2(file)

/**
 * 对象数据的序列化反序列化
 */
// 序列化
void fun3(File file) {
    def hello = 'hello'
    def b = true

    file.withDataOutputStream {out ->
        out.writeBoolean(b)
        out.writeUTF(hello)
    }
}

// 反序列化
void fun4(File file) {
    file.withDataInputStream {input ->
        println(input.readBoolean()) // true
        println(input.readUTF()) // hello
    }
}

// 序列化/反序列化一个对象
void fun5(File file) {
    def person = new Person('name', 10)

    file.withObjectOutputStream {out ->
        out.writeObject(person)
    }

    file.withObjectInputStream {input ->
        def object = input.readObject()
        println(object.name)
        println(object.age)
    }
}



//fun3(file)
//fun4(file)
//fun5(new File('d:/xiaoyu.txt'))


/**
 * 遍历文件
 */

def dir = new File(".")

// 不会 recurse
void list1(File file) {
    file.eachFile {f ->
        println(f.name)
    }
}

// 正则查找
void list2(File file) {
    file.eachFileMatch(~/^package-info.java$/) { f ->
        println(f.name)
    }
}

// 会递归
void list3(File file) {
    file.eachFileRecurse {f ->
        println(f.name)
    }
}

// 只针对 文件 执行闭包代码, 对于目录, 不执行 closure
void list4(File file) {
    file.eachFileRecurse(FileType.FILES) {f ->
        println(f.name)
    }
}

void list5(File file) {
    file.traverse {f ->
        if (file.directory && file.name == 'bin') {
            FileVisitResult.TERMINATE // 若遍历到名为 bin 的目录, 退出遍历
        } else {
            println(f.name)
            FileVisitResult.CONTINUE // 继续遍历
        }
    }
}

//list1(dir)
//list2(dir)

/**
 * 执行命令行命令
 */

void comm1() {
    def process = 'ipconfig'.execute()
    println(process.text) // 中文乱码
}

// Process 对象有  in/out/err 流, in 为标准输入流, 对应着command的输出结果; out 代表标准输出, 我们可以通过out发送数据给command
// "cmd /c dir" - Windows平台shell有些builtin command, 需要通过 cmd 调用
void comm2() {
    def process = 'ipconfig'.execute()
    process.in.eachLine('gbk') {line ->
        println(line)// 使用 windows 默认 gbk 编码
    }
}

// pipe line 管道
//todo
void comm3() {
    proc1 = 'ipconfig'.execute()
    proc2 = 'grep'
}

//comm1()
comm2()




