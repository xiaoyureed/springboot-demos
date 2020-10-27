package io.github.xiaoyureed.single.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");// 和 direct 类似， 只是 routingKey 更加灵活

            Scanner scan = new Scanner(System.in);
            System.out.println("输入：");
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                int    indexOfSeparator    = line.indexOf(" ");
                String routingKey = line.substring(0, indexOfSeparator);// routingKey必须由点分开
                        // 【*】表示 一个单词(不是字符)
                        // 【#】表示0个或多个单词
                String message = line.substring(indexOfSeparator + 1);

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }

        }
    }

}
