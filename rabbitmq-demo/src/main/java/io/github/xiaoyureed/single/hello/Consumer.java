package io.github.xiaoyureed.single.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * 先运行
 *
 * @author xiaoyu
 * @since 1.0
 */
public class Consumer {
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Provider.HOST);
        //factory.setUsername("demo");
        //factory.setPassword("demo");
        //factory.setPort(5672);
        //factory.setVirtualHost("/");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 这里没有使用 try-with-resource statement；因为希望consumer能够持续监听，不要退出

        channel.queueDeclare(Provider.QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 回调， 有消息的话就执行
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        boolean autoAck = true;
        channel.basicConsume(Provider.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }
}
