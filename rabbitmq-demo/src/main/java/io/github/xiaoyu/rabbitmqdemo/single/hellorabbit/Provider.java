package io.github.xiaoyu.rabbitmqdemo.single.hellorabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Provider {
    private static final String HOST       = "localhost";
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        //factory.setUsername("demo");
        //factory.setPassword("demo");
        //factory.setPort(5672);
        try (
                Connection connection = factory.newConnection();// 会自动关闭
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);//idempotent  幂等的
                            // it will only be created if it doesn't exist already
            String message = "Hello World!";
            // exchange = “”， 表示采用默认的exchange
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }


    }
}
