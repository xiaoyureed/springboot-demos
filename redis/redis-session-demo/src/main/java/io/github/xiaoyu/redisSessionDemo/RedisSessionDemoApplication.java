package io.github.xiaoyu.redisSessionDemo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class RedisSessionDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSessionDemoApplication.class, args);
    }

}
/**
 * @author xiaoyu
 * @since 1.0
 */
@RestController
class UserController {

    @GetMapping("/uid")
    public String getUid(HttpSession session) {
        String uid = (String) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID().toString();
            System.out.println("[x] first access, uid:" + uid);
        }
        System.out.println("[x] get uid:" + uid);
        session.setAttribute("uid", uid);
        return session.getId();
    }

    @GetMapping("/get")
    public User getUser() throws Exception {
        return findUser();
    }

    /**
     * 当设置了@Cacheable的key时，spring不会使用用户配置
     * 的KeyGenerator进行key的生成，而是直接使用注解中的, 此时需要保证不同方法的key的唯一性
     * 指定要按照 SpEL 表达式编写, 如 key = "{#root.methodName,#id}"
     *
     * 注解 @Cacheable 的 value 为 缓存名, 同一个缓存下的所有key值，在redis中会保存在名为 缓存名~keys的zset中。
     *
     * https://blog.csdn.net/dreamhai/article/details/80642010
     */
    @GetMapping("/getWithCache")
    @Cacheable("user-key")//缓存到 Redis 中的 key
    public User getUserWithCache() throws Exception {
        return findUser();
    }

    private User findUser() throws InterruptedException {
        User user = new User("IDIDIDID", "Jackson", "mark, mark sdfds");
        Thread.sleep(3000);
        return user;
    }
}

/**
 * @author xiaoyu
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class User implements Serializable {
    private String id;
    private String name;
    private String remark;
}

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
@EnableCaching
class RedisConfig extends CachingConfigurerSupport {
    /**
     * 默认情况下，key值是有问题，当方法入参相同时，key值也相同，这样会造成不同的方法读取相同的缓存，从而造成异常
     *
     * 覆盖 key generator
     *
     * 自定义的 key值为className+methodName+参数值列表
     *
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append("#");
            sb.append(method.getName()).append("(");
            Stream.of(params).map(Object::toString).forEach(
                    paramStr -> sb.append(paramStr).append(","));
            sb.append(")");
            return sb.toString();
        };
    }

    /**
     * 注册自定义类型的redis template
     * <p>
     * 需要 common-pool2 提供池化
     */
    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory factory,
                                                                   ObjectMapper objectMapperCustomized) {
        /*
         * 配置自己的redisTemplate
         * StringRedisTemplate 默认使用使用StringRedisSerializer来序列化
         * RedisTemplate 默认使用JdkSerializationRedisSerializer来序列化
         */
        RedisTemplate<String, Object> stringObjectRedisTemplate = new RedisTemplate<>();
        stringObjectRedisTemplate.setConnectionFactory(factory);

        // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // jackson2JsonRedisSerializer.setObjectMapper(objectMapperCustomized);
        // stringObjectRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // stringObjectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        // stringObjectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // stringObjectRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        stringObjectRedisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        stringObjectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringObjectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringObjectRedisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return stringObjectRedisTemplate;
    }


    /**
     * 缓存默认超时配置
     */
    @Override
    public CacheManager cacheManager() {
        // RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 设置默认过期时间为60秒
        // cacheManager.setDefaultExpiration(60);
        // return cacheManager;
        return null;
    }


    @Bean
    public ObjectMapper objectMapperCustomized() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }
}

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)// 30天失效,
// 原来的server.session.timeout 属性不再生效
class SessionConfig {
}
