package groovy.script

/**
 * @author xiaoyu* date: 2020/3/16
 */
class Hello {
    def content

    def hi(String who) {
        println("hello ${who} -> ${content}")
    }
}

def func(String who) {
    def hello = new Hello()
    hello.content = "fuck"
    hello.hi(who)
}

