package io.github.xiaoyu.rabbitmqdemo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
public class RabbitConfig {
    @Bean(name = "hello")
    public Queue queueHello() {
        return new Queue("hello");
    }

}
