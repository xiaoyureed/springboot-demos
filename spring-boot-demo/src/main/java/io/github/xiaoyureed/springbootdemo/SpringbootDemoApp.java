package io.github.xiaoyureed.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author xiaoyu
 * date: 2020/3/21
 */
@SpringBootApplication
public class SpringbootDemoApp {
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
}
