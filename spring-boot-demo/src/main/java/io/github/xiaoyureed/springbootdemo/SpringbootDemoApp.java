package io.github.xiaoyureed.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author xiaoyu
 * date: 2020/3/21
 */
@SpringBootApplication
public class SpringbootDemoApp {

    /**
     * springboot在启动的时候需要检测当前项目是否是一个web项目，
     * 检测方式为判断classpath中是否有以下final变量中定义的两个参数
     */
    private static final String[] WEB_ENVIRONMENT_CLASSES =
            new String[]{"javax.servlet.Servlet", "org.springframework.web.context.ConfigurableWebApplicationContext"};

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootDemoApp.class, args);
        ConfigurableEnvironment        env     = context.getEnvironment();

        String[] activeProfiles = env.getActiveProfiles();
        String   port           = env.getProperty("server.port");

        System.out.println("==============================");
        System.out.println("active profiles: " + Arrays.toString(activeProfiles));
        System.out.println("local uri: http://localhost:" + port);
        System.out.println("local area network uri: http://" + localAreaNetwork() + ":" + port);
        System.out.println("==============================");
    }

    private static String localAreaNetwork() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "get local area network error";
        }
    }

    private boolean deduceWebEnv() {
        //https://blog.csdn.net/txba6868/article/details/80732475
        return true;
    }
}

@RestController
class DemoController {

    /**
     * file upload
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> upload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile aa = req.getFile("aa");
        MultipartFile bb = req.getFile("bb");
        aa.transferTo(Paths.get(System.getProperty("user.home") + "/tmp").toFile());

        return ResponseEntity.ok("ok");
    }
}
