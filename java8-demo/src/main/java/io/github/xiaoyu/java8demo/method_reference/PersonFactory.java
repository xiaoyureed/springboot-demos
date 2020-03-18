package io.github.xiaoyu.java8demo.method_reference;

/**
 * @author xiaoyu
 * @since 1.0
 */
public interface PersonFactory<T extends Person> {
    T create(String first, int age);
}
