package io.github.xiaoyu.dockercomposedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class DockerComposeDemoApplication {

    private final UserRepository repository;

    @Autowired
    public DockerComposeDemoApplication(UserRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DockerComposeDemoApplication.class, args);
    }

    @GetMapping("/")
    public String index(HttpServletRequest req) {
        String remoteAddr = req.getRemoteAddr();
        User   user       = repository.findByIp(remoteAddr);
        if (user == null) {
            user = new User();
            user.setIp(remoteAddr);
            user.setTimes(1);
        } else {
            user.setTimes(user.getTimes()+1);
        }
        repository.save(user);

        return "访客ip：[" + user.getIp() + "], 次数：" + user.getTimes();
    }
}
