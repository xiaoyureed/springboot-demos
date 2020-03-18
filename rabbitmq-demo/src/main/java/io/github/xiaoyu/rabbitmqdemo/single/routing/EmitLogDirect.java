package io.github.xiaoyu.rabbitmqdemo.single.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);// exchange类型

            Scanner scan = new Scanner(System.in);
            System.out.println("输入：");
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                int    indexOfSeparator    = line.indexOf(" ");
                String routingKey = line.substring(0, indexOfSeparator);
                String message = line.substring(indexOfSeparator + 1);

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }

        }
    }

}
