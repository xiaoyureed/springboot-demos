package io.github.xiaoyureed.demospringbootstartertest.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author xiaoyu
 * date: 2020/3/18
 */
@Configuration
public class RandomGenerationAutoConfig {

    @Bean
    @Conditional({RandomConditionBool.class})
    public RandomGenerator<Boolean> randomGeneratorBoolean() {
        return new RandomGenerator<>(() -> new Random().nextBoolean());
    }

    @Bean
    @Conditional({RandomConditionInt.class})
    public RandomGenerator<Integer> randomGeneratorInteger() {
        return new RandomGenerator<>(() -> new Random().nextInt());
    }
}
