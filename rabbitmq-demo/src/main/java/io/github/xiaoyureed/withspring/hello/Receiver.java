package io.github.xiaoyureed.withspring.hello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/27
 */
@Component
/* 也能用于方法上, 方法的参数:
 * 参数:
 * - Message 可以通过 msg.getbody() 拿到 发送的数据字节数组
 * - T 发送的实体类型, spring 帮助直接封装到参数里了, 这里可以直接使用
 * - channel
 */
@RabbitListener(queues = {"hello"})
public class Receiver {
    static {
        System.out.println(">>> receiver init");
    }


    /**
     * 方法参数 可以有多个, 类似 @RabbitListener
     *
     * 而且同一个 @RabbitListener 可以有多个  Handler, 分别处理不同类型的数据
     * 比如队列中可能发送的有 User, 也可能有 Person 类型的数据, 分别用不同的 Handler 处理即可
     *
     * @param in
     */
    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [x] Received '" + in + "'");
    }

}
