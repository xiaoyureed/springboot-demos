package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.functional_interface.Converter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class FunctionalInterfaceTest {
    @org.junit.Test
    public void testFunctionalInterface() {
        Converter<String, Integer> converter = Integer::valueOf;
        Integer                    result    = converter.convert("123");
        System.out.println(result);
        Assertions.assertThat(result).isEqualTo(123);
    }

    @Test
    public void testAndThen() {
    }
}
