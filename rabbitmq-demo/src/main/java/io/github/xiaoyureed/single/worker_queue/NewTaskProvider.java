package io.github.xiaoyureed.single.worker_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class NewTaskProvider {

    public static final String HOST = "localhost";
    public  static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        ) {

            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

            //String message = String.join(" ", argv);// 拼接命令行参数
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("/n");
            System.out.print("输入消息：");
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();

                channel.basicPublish("", TASK_QUEUE_NAME,
                        // msg 持久化配置
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }


        }
    }

}
