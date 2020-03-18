package io.github.xiaoyureed.demospringbootstartertest.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author xiaoyu
 * date: 2020/3/18
 */
public class RandomConditionBool implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        return "boolean".equalsIgnoreCase(conditionContext.getEnvironment()
                .getProperty("random-generator.type"));
    }
}
