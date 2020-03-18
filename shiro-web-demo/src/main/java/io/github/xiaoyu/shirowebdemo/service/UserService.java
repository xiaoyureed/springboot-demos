package io.github.xiaoyu.shirowebdemo.service;

import io.github.xiaoyu.shirowebdemo.dto.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Service
public class UserService {
    public User findUserByName(String name) {
        User user = new User();
        user.setUid(new Random().nextLong());
        user.setName(name);
        user.setNick(name + "Nick");
        user.setPwd("J/ms7qTJtqmysekuY8/v1TAS+VKqXdH5sB7ulXZOWho=");//密码明文是123456
        user.setSalt("wxKYXuTPST5SG0jMQzVPsg==");
        user.setCreate(new Date());

        return user;
    }
}
