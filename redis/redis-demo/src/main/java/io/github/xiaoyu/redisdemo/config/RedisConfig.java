package io.github.xiaoyu.redisdemo.config;

import io.github.xiaoyu.redisdemo.dto.User;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 覆盖 key generator
     * 原始是返回 null
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for(Object o: params) {
                sb.append(o.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 注册自己的类型的redis template
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, User> stringUserRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, User> stringUserRedisTemplate = new RedisTemplate<>();
        stringUserRedisTemplate.setConnectionFactory(factory);
        return stringUserRedisTemplate;
    }
}
