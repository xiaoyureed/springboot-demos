//package io.github.xiaoyureed.rabbitmqdemo;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author xiaoyu
// * @since 1.0
// */
//@Component
//public class Receiver2 {
//    @RabbitListener(queues = "queue_2")
//    public void process(String msg) {
//        System.out.println("receiver2: " + msg);
//    }
//
//    @RabbitListener(queues = {"hello_queue"})
//    public void processHelloQueue(String msg) {
//        System.out.println(">>> receive msg from hello_queue: " + msg);
//    }
//}
