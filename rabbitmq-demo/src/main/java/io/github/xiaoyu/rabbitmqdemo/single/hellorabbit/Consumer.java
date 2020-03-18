package io.github.xiaoyu.rabbitmqdemo.single.hellorabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 先运行
 * @author xiaoyu
 * @since 1.0
 */
public class Consumer {
    private final static String QUEUE_NAME = "hello";
    private static final String HOST = "localhost";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        //factory.setUsername("demo");
        //factory.setPassword("demo");
        //factory.setPort(5672);
        //factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel    channel    = connection.createChannel();
            // 这里没有使用 try-with-resource statement；因为希望consumer能够持续监听，不要退出

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);// 幂等的
                        // 确保queue存在， consumer可能先启动
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 回调， 有消息的话就执行
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
