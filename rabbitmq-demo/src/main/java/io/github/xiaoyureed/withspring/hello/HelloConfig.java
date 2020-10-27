package io.github.xiaoyureed.withspring.hello;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
@Configuration
@EnableScheduling
public class HelloConfig {

    /**
     * 双方都要声明一个 queue
     * @return
     */
    @Bean
    public Queue helloQ() {
        System.out.println(">>> queue [hello] init");
        return new Queue("hello");
    }


}
