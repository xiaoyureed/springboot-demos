package io.github.xiaoyu.java8demo.functional_interface;

/**
 * @author xiaoyu
 * @since 1.0
 */
@FunctionalInterface// optional
public interface Converter<S, T> {
    T convert(S source);

    //converter的实例默认继承自Object, 而object中有equals方法的实现
    // 所以converter仍然是函数式接口
    boolean equals(Object object);
}
