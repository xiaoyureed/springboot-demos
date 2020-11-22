package io.github.xiaoyureed.springbootinterceptor;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/12
 */
@Component
public class RedisService {
    private static final ConcurrentMap<String, Account> cache = new ConcurrentHashMap<>(2);

    public void put(String token, Account account) {
         cache.put(token, account);
    }

    public Account get(String token) {
        return cache.get(token);
    }

    public boolean exist(String token) {
        Account account = this.get(token);
        return account != null;
    }
}
