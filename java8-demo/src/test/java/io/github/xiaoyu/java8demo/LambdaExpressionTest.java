package io.github.xiaoyu.java8demo;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class LambdaExpressionTest {

    @Test
    public void testLambda() {
        List<String> names = Arrays.asList("a", "b", "c", "d");
        names.sort((a, b) -> b.compareTo(a));// reverse order
        Collections.sort(names, (x, y) -> y.compareTo(x));// 等价

        Assertions.assertThat(names).containsSequence("d", "c", "b", "a");
    }
}
