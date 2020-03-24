package io.github.xiaoyu.rabbitmqdemo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Component
@RabbitListener(queues = "queue_1")
public class Receiver1 {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("receiver1: " + hello);
    }

    @RabbitHandler
    public void process(User user) {
        System.out.println(">>> receiver1: " + user);
    }
}
