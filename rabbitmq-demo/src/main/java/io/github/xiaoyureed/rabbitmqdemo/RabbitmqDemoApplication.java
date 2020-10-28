package io.github.xiaoyureed.rabbitmqdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaoyu
 */
@SpringBootApplication(scanBasePackages = {"io.github.xiaoyureed.withspring.routing"})
public class RabbitmqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner keepAlive(ConfigurableApplicationContext context) {
        // keep the application alive for  twenty seconds
        return args -> {
            Thread.sleep(20 * 1000);
            context.close();
        };
    }
}
