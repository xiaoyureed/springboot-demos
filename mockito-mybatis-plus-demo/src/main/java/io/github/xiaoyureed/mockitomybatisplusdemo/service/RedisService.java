package io.github.xiaoyureed.mockitomybatisplusdemo.service;

import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author xiaoyu
 * date: 2020/1/22
 */
@Component
public class RedisService {
    private static final String USER_INFO_PREFIX = "user_info:";

    /**
     * save(USER_INFO_PREFIX + uuid, toJson(user))
     * @param user
     * @return uuid
     */
    public String saveLoginInfo(User user) {
        // ...
        return UUID.randomUUID().toString();
    }
}
