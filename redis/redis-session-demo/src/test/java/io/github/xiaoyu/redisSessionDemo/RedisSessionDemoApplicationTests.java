package io.github.xiaoyu.redisSessionDemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class RedisSessionDemoApplicationTests {

    /**
     * 不必注册 ， starter提供了
     *
     * just for String-String,
     *
     * 不推荐使用, 会有 双引号问题
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapperCustomized;


    @Test
    public void testRedis() throws JsonProcessingException {
        redisService.set("test:key", "test.value");

        User user = new User("123", "xy", "remark");
        redisService.set("user:123", user);


        String value1 = redisService.get("test:key", String.class);
        assertThat(value1).isEqualTo("test.value");
        User user1 = redisService.get("user:123", User.class);
        assertThat(user1.getId()).isEqualTo("123");
        Object o = redisService.get("user:123"); // 是一个 map
        System.out.println(o);

        redisService.set("user:123:json", objectMapperCustomized.writeValueAsString(user));
        String userJson = redisService.get("user:123:json", String.class);
        System.out.println(userJson);
    }

}
