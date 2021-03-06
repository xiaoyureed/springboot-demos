package io.github.xiaoyureed.withspring.worker_queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
public class Sender {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    /**
     * 当前要发送的消息有几个 dot
     */
    AtomicInteger dots = new AtomicInteger(0);

    /**
     * 发送消息序号
     */
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        // dot 数目最多到 3
        if (dots.incrementAndGet() == 4) {
            // reset to 1
            dots.set(1);
        }
        // 添加 dot 到 msg
        for (int i = 0; i < dots.get(); i++) {
            builder.append('.');
        }
        builder.append(count.incrementAndGet());
        String message = builder.toString();
        template.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
