package io.github.xiaoyu.rabbitmqdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqDemoApplicationTests {

    @Autowired
    private Sender sender;

    @Test
    public void testHello() {
        sender.send1("消息1" + new Date());
    }

    @Test
    public void testOneToMany() {
        for (int i = 0; i < 50; i++) {
            sender.send1("消息 " + i);
        }
    }

    @Test
    public void testTopicSend() {
        sender.sendWithTopic("消息 1, topic[topic.message]", "topic.message");
        sender.sendWithTopic("消息 2, topic[topic.haha.hoho]", "topic.haha.hoho");
    }

}
