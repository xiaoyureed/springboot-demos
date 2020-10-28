package io.github.xiaoyureed.withspring.routing;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
    public DirectExchange directExchange() {
        return new DirectExchange("tut.direct");
    }
}

@Profile("receiver")
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
    public Binding binding2a(DirectExchange directExchange,
                            Queue autoDeleteQueue2){
        return BindingBuilder.bind(autoDeleteQueue2).to(directExchange).with("green");
    }
    @Bean
    public Binding binding2b(DirectExchange directExchange,
                             Queue autoDeleteQueue2){
        return BindingBuilder.bind(autoDeleteQueue2).to(directExchange).with("black");
    }

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding binding1a(DirectExchange directExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(directExchange).with("orange");
    }

    @Bean
    public Binding binding1b(DirectExchange directExchange, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(directExchange).with("black");
    }

    @Bean
    public Receiver receiver(){
        return new Receiver();
    }
}

/**
 * provider need setup exchange, sender
 * consumer need setup queue, binding, exchange, receiver
 */
@Profile("sender")
@Configuration
class SenderConfig {
    @Bean
    public Sender sender() {
        return new Sender();
    }
}


