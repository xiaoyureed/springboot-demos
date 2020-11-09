package io.github.xiaoyu.dockerdemo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoyu
 * @since 1.0
 */
@RestController
public class DockerController {
    @GetMapping("/")
    public String index() {
        return "Hello docker.";
    }
}
