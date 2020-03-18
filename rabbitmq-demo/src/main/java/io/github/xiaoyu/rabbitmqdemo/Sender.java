package io.github.xiaoyu.rabbitmqdemo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Component
public class Sender {
    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public Sender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * string类型的字符串
     */
    public void send1(String msg) {
        this.rabbitTemplate.convertAndSend("hello", msg);
        System.out.println("sender: 发送了 " + msg);
    }

    /**
     * 带 topic 发送
     * @param msg
     * @param routingKey
     */
    public void sendWithTopic(String msg, String routingKey) {
        this.rabbitTemplate.convertAndSend("exchange", routingKey, msg);
    }
}
