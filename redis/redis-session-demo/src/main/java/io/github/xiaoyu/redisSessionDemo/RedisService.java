package io.github.xiaoyu.redisSessionDemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyu
 * date: 2020/3/26
 *
 * https://www.jianshu.com/p/b9154316227e
 */
@Component
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapperCustomized;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapperCustomized) {
        this.redisTemplate = redisTemplate;
        this.objectMapperCustomized = objectMapperCustomized;
    }


    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e) {
            log.error(">>> error when get from redis (key = {})", key);
            e.printStackTrace();
            return null;
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = this.get(key);
        // if (clazz.isAssignableFrom(String.class)) {
        //     return
        // }
        return objectMapperCustomized.convertValue(value, clazz);
    }

    public boolean set(String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // if (value instanceof String) {
        //     ops.set(key, value);
        // } else {
        //     ops.set(key, toJson(value));
        // }
        ops.set(key, value);
        return true;
    }

    //////////////////////////////
    //  object 作为 string 存储
    //////////////////////////////

    public boolean setWithJson(String key, Object value) {
        return this.set(key, toJson(value));
    }

    public <T> T getWithJson(String key, Class<T> clazz) {
        String json = this.get(key, String.class);
        try {
            return objectMapperCustomized.readValue(
                    json.getBytes(StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson(Object value) {
        try {
            return objectMapperCustomized.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(">>> error when write value as string, value = {}", value);
            e.printStackTrace();
            return "";
        }
    }

    /**
     * no expiration limitation if timeout <= 0
     */
    public boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String... keys) {
        if (keys.length == 0) {
            log.warn(">>> cannot delete in redis");
            return false;
        }
        if (keys.length ==1) {
            redisTemplate.delete(keys[0]);
        }
        else {
            redisTemplate.delete(Arrays.asList(keys));
        }
        return true;
    }

    public boolean hasKey(String key){
        Boolean aBoolean = redisTemplate.hasKey(key);
        if (aBoolean == null) {
            return false;
        }
        return aBoolean;
    }

    public long getExpires(String key, TimeUnit timeUnit) {
        Long expires = redisTemplate.getExpire(key, timeUnit);
        if (expires == null) {
            throw new RuntimeException(">>> error when getting expires");
        }
        return expires;
    }

    public boolean expire(String key, long durationInSeconds, TimeUnit timeUnit) {
        redisTemplate.expire(key, durationInSeconds, timeUnit);
        return true;
    }

    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }


    ////////////////////////////////////////////////////
    //       hash
    ////////////////////////////////////////////////////

    public Object getForHash(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> getForHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public boolean setForHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return true;
    }

    public boolean setForHash(String key, Map<String,Object> map){
        return this.setForHash(key, map, 0, TimeUnit.SECONDS);
    }

    public boolean setForHash(String key, Map<String, Object> map,
                              long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            this.expire(key, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteForHash(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
        return true;
    }

}
