package io.github.xiaoyureed.mockitomybatisplusdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.dto.BaseResp;
import io.github.xiaoyureed.mockitomybatisplusdemo.pojo.po.User;
import io.github.xiaoyureed.mockitomybatisplusdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author xiaoyu
 * date: 2020/1/24
 */
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/user/session", method = RequestMethod.POST)
    public ResponseEntity<BaseResp<String>> login(RequestEntity<User> req) {
        User   body  = req.getBody();
        String token = userService.login(body.getName(), body.getPwd());
        return new ResponseEntity<>(new BaseResp<>(token), HttpStatus.OK);
    }

}

