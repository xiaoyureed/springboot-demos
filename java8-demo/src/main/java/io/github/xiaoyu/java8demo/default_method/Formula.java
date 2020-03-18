package io.github.xiaoyu.java8demo.default_method;

/**
 * @author xiaoyu
 * @since 1.0
 */
public interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
