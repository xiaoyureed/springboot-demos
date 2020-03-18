package io.github.xiaoyu.rabbitmqdemo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Component
@RabbitListener(queues = "queue_2")
public class Receiver2 {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("receiver2: " + msg);
    }
}
