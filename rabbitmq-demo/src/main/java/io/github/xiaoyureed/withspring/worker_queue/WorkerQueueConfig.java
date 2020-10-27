package io.github.xiaoyureed.withspring.worker_queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * consumer 重复注册多个即可
     *从 同个 queue 消费数据, 不会重复消费
     */
    private static class ReceiverConfig {
        @Bean
        public Receiver receiver1() {
            return new Receiver(1);
        }
        @Bean
        public Receiver receiver2() {
            return new Receiver(2);
        }

    }

    private static class SenderConfig {
        @Bean
        public Sender sender() {
            return new Sender();
        }
    }
}
