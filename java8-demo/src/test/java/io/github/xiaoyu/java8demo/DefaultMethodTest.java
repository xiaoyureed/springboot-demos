package io.github.xiaoyu.java8demo;

import io.github.xiaoyu.java8demo.default_method.Formula;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class DefaultMethodTest {
    @Test
    public void testDefaultMethod() {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        assertThat(formula.calculate(100)).isEqualTo(100);
        assertThat(formula.sqrt(16)).isEqualTo(4);
    }
}
