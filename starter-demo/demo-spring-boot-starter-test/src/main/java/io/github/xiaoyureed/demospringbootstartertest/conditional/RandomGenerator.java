package io.github.xiaoyureed.demospringbootstartertest.conditional;

import java.util.function.Supplier;

/**
 * @author xiaoyu
 * date: 2020/3/18
 */
public class RandomGenerator<T> {

    private final Supplier<T> supplier;

    public RandomGenerator(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        return supplier.get();
    }
}
