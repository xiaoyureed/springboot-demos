package io.github.xiaoyu.rabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Configuration
public class RabbitConfig {

    @Bean
    public AmqpTemplate template() {
        RabbitTemplate template = new RabbitTemplate();
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public Queue helloQueue() {
        return new Queue("hello_queue");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue_1");
    }

    @Bean
    public Queue queue2() {
        return new Queue("queue_2");  // 需要指定 name
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("exchange");  // 需要指定 name
    }

    // binding exchange and queue with routing key
    @Bean
    public Binding bindingQueue1(Queue queue1, TopicExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange).with("topic.message");
    }

    @Bean
    public Binding bindingQueue2(Queue queue2, TopicExchange exchange) {
        return BindingBuilder.bind(queue2).to(exchange).with("topic.#");
    }

}
