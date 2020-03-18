package io.github.xiaoyu.shirowebdemo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Controller
public class LoginController {
    @RequestMapping("/login.html")
    public String loginTemplate() {

        return "login";
    }
}
