package com.example.dockerspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoyu
 */
@SpringBootApplication
@RestController
public class DockerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerSpringBootApplication.class, args);
    }

    @RequestMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello docker");
    }
}
