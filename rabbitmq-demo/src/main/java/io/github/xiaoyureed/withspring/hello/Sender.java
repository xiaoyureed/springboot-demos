package io.github.xiaoyureed.withspring.hello;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String hello = "hello spring rabbit";
        rabbitTemplate.convertAndSend(queue.getName(), hello);
        System.out.println(" [x] Sent '" + hello + "'");
    }
}
