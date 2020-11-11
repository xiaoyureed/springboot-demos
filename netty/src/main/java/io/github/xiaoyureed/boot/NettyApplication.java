package io.github.xiaoyureed.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xiaoyu
 */
@SpringBootApplication
@ComponentScan("io.github.xiaoyureed.springboot_heart_beat")
public class NettyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
        String[] profiles = context.getEnvironment().getActiveProfiles();
        for (String profile : profiles) {
            if (profile.contentEquals("server")) {
                System.out.println("==========");
                System.out.println("\tserver");
                System.out.println("==========");
            } else if (profile.contentEquals("client0")) {
                System.out.println("==========");
                System.out.println("\tclient0");
                System.out.println("==========");
            } else if (profile.contentEquals("client1")) {
                System.out.println("==========");
                System.out.println("\tclient1");
                System.out.println("==========");
            }
            break;
        }
    }

}
