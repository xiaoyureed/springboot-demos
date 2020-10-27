//package io.github.xiaoyureed.rabbitmqdemo;
//
//import io.github.xiaoyureed.withspring.hello.Sender;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.stream.IntStream;
//
//@SpringBootTest
//public class RabbitmqDemoApplicationTests {
//
//    @Autowired
//    private Sender sender;
//
//    @Test
//    public void testHello() {
//        IntStream.rangeClosed(1, 50).forEach(index -> {
//            String msg = "msg" + index;
//            // sender.sendMessage(msg);
//            sender.send1(msg);
//        });
//    }
//
//    @Test
//    public void testTopicSend() {
//        sender.sendWithTopic("消息 1, topic[topic.message]", "topic.message");
//        sender.sendWithTopic("消息 2, topic[topic.haha.hoho]", "topic.haha.hoho");
//    }
//
//}
