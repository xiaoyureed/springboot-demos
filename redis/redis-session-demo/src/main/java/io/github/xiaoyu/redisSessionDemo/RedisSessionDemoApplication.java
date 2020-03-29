package io.github.xiaoyu.redisSessionDemo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.UUID;

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
 * 必须实现 序列化接口
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
     * 覆盖 key generator
     * 原始是返回 null
     *
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object o : params) {
                sb.append(o.toString());
            }
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

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapperCustomized);
        stringObjectRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        stringObjectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringObjectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringObjectRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return stringObjectRedisTemplate;
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
