package io.github.xiaoyureed.mockitomybatisplusdemo.service;

import io.github.xiaoyureed.mockitomybatisplusdemo.mapper.UserMapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author xiaoyu
 * date: 2020/1/22
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private RedisService redisService;

    public String login(String name, String pwd) {
        User condition = new User();
        condition.setName(name);
        condition.setPwd(pwd);
        User user = userMapper.conditionalQuery(condition);
        if (user == null) {
            throw new RuntimeException("not login");
        }
        taskExecutor.execute(() -> System.out.println(String.format("user %s logged in", name)));
        return redisService.saveLoginInfo(user);
    }
}
