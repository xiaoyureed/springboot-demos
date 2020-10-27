package io.github.xiaoyureed.single.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Provider {
    public static final String HOST = "localhost";
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        //factory.setUsername("demo");
        //factory.setPassword("demo");
        //factory.setPort(5672);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            // queue 声明 , 是 idempotent幂等 的;it will only be created if it doesn't exist already;
            //参数:
            //- q_name,队列名称
            //- durable/是否持久化,队列会保存磁盘, 重启不丢失消息
            //- exclusive/是否排他)一个队列声明为排他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。
            //     排它是基于连接可见的，同一个连接不同信道是可以访问同一连接创建的排它队列，
            //    “首次”是指如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列，
            //   即使这个队列是持久化的，一旦连接关闭或者客户端退出，该排它队列会被自动删除，
            //    这种队列适用于一个客户端同时发送与接口消息的场景。
            //- autoDelete :设置是否自动删除。(自动删除的前提是至少有一个consumer 连接过这个 queue, 断开后 queue 自动删除)
            //- arguments ：设置队列的一些其它参数。如x-message-ttl,x-expires
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            // 参数:
            //
            //- exchange 表示交换器名称, “”， 表示采用默认的exchange
            //- routingKey 路由键
            //- props 持久化参数
            //- 字节数组
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }


    }
}
