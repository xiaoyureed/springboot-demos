package io.github.xiaoyureed.withspring.publisher_subscribe;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
@Configuration
public class Config {
    /**
     * broadcasting msg, 两边都要
     * @return
     */
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("tut.fanout");
    }


}


@Configuration
class ReceiverConfig {
    /**
     * 匿名临时 queue
     * @return
     */
    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding2(FanoutExchange fanout,
                            Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }
}

@Configuration
class SenderConfig {
    @Bean
    public Sender sender() {
        return new Sender();
    }
}


