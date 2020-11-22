package io.github.xiaoyureed.springbootinterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/12
 */
@Component
public class AccessTokenVerifyInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        Account exist = redis.get(token);
        if (exist != null) {
            request.setAttribute("login_user", exist);
            return true;
        }
        throw new AuthException();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
