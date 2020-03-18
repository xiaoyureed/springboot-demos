package io.github.xiaoyu.shirowebdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "io.github.xiaoyu.shirowebdemo.config",
        "io.github.xiaoyu.shirowebdemo.service",
        //"io.github.xiaoyu.shirowebdemo.web",
        "io.github.xiaoyu.shirowebdemo.rest",
})
public class ShiroWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroWebDemoApplication.class, args);
    }

}
