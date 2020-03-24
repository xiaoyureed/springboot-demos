package io.github.xiaoyu.rabbitmqdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqDemoApplicationTests {

    @Autowired
    private Sender sender;

    @Test
    public void testHello() {
        IntStream.rangeClosed(1, 50).forEach(index -> {
            String msg = "msg" + index;
            // sender.sendMessage(msg);
            sender.send1(msg);
        });
    }

    @Test
    public void testTopicSend() {
        sender.sendWithTopic("消息 1, topic[topic.message]", "topic.message");
        sender.sendWithTopic("消息 2, topic[topic.haha.hoho]", "topic.haha.hoho");
    }

}
