package io.github.xiaoyu.redisdemo;

import io.github.xiaoyu.redisdemo.dto.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    /**
     * 不必注册 ， starter提供了
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 不必注册， starter 提供了
     */
    @Autowired
    private RedisTemplate redisTemplate;

    //@Autowired
    //private RedisTemplate<String, User> redisTemplate;

    @Test
    public void testRedisString() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("test", "111");
        Assert.assertEquals("111", stringStringValueOperations.get("test"));
    }

    @Test
    public void testRedisObject() throws InterruptedException {
        User            user = new User("ididididid", "Jack", "remark-remark-remark");
        ValueOperations<String , User> ops  = redisTemplate.opsForValue();
        ops.set("user", user);
        ops.set("user.timeout", user,1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        Boolean userKey = redisTemplate.hasKey("user");
        Boolean userTimeout = redisTemplate.hasKey("user.timeout");
        Assert.assertEquals(true, userKey);
        Assert.assertEquals(false, userTimeout);
    }

}
