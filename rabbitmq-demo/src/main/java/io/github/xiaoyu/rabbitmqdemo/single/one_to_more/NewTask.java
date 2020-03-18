package io.github.xiaoyu.rabbitmqdemo.single.one_to_more;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            //String message = String.join(" ", argv);// 拼接命令行参数
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("/n");
            System.out.println("输入消息：");
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();

                channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,// msg 持久化配置
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }


        }
    }

}
