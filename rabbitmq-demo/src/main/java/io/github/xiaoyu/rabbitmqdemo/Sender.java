package io.github.xiaoyu.rabbitmqdemo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Component
public class Sender {
    private final AmqpTemplate template;

    @Autowired
    public Sender(AmqpTemplate template) {
        this.template = template;
    }

    /**
     * 使用 MessageBuilder 构建消息
     */
    public void sendMessage(String msg) {
        template.send(MessageBuilder.withBody(msg.getBytes(StandardCharsets.UTF_8)).build());
        System.out.println("send msg: " + msg);
    }

    /**
     * string类型的字符串
     */
    public void send1(String msg) {
        this.template.convertAndSend("hello_queue", msg);
        System.out.println("sender: 发送了 " + msg);
    }

    /**
     * 带 exchange 和 routing key 发送
     */
    public void sendWithTopic(String msg, String routingKey) {
        this.template.convertAndSend("exchange", routingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 消息持久化
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        });
    }
}
