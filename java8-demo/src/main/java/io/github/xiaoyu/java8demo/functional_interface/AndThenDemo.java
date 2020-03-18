package io.github.xiaoyu.java8demo.functional_interface;

/**
 * @author xiaoyu
 * @date 2019/5/17
 */
@FunctionalInterface
public interface AndThenDemo {
    void doSomething(String str);

    default void andThen(AndThenDemo andThenDemo) {
        // this.doSomething();
    }
}
