package io.github.xiaoyureed.withspring.worker_queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
@Configuration
public class WorkerQueueConfig {
    @Bean
    public Queue helloQ() {
        return new Queue("hello");
    }

}

/**
 * consumer 重复注册多个即可
 * queue 只有一个, 消息不会重复消费
 */
@Configuration
@Profile("receiver")
class ReceiverConfig {
    @Bean
    public Receiver receiver1() {
        return new Receiver(1);
    }
    @Bean
    public Receiver receiver2() {
        return new Receiver(2);
    }

}

@Configuration
@Profile("sender")
@EnableScheduling
class SenderConfig {
    @Bean
    public Sender sender() {
        return new Sender();
    }
}
