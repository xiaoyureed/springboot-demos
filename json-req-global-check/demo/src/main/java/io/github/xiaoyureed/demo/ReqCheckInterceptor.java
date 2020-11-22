package io.github.xiaoyureed.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/13
 */
//@Component
public class ReqCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletInputStream is = request.getInputStream();
        byte[] body = is.readAllBytes();
        String reqJson = new String(body);

        System.out.println(">>> reqJson = " + reqJson);

        return true;
    }
}
