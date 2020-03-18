package groovy.clazz

/**
 * @author xiaoyu* date: 2020/3/16
 */
class Cat {
    def name
    def id
    String toString() {
        "${this.class.name}{ ${id}, ${name} }"
    }

    def hi() {
        println "I am a cat [${name}]"
    }
}
