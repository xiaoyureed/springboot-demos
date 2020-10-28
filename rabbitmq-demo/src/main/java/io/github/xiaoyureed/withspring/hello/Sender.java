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
        // 有多个重载方法, 可以传递:
        // queue
        // content
        // route key , exchange
        // correlationData 确认回调函数会接受到这个对象
        rabbitTemplate.convertAndSend(queue.getName(), hello);
//        使用 MessageBuilder 构建消息
        //template.send(MessageBuilder.withBody(msg.getBytes(StandardCharsets.UTF_8)).build());
        System.out.println(" [x] Sent '" + hello + "'");

        // 对构造 的 message 做一些处理, 比如添加头信息
//        this.template.convertAndSend("exchange", routingKey, msg, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                // 消息持久化
//                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    // 头信息
//                message.getMessageProperties().getHeaders().put("tenantId", tenantId);
//                return message;
//            }
//        });
    }
}
