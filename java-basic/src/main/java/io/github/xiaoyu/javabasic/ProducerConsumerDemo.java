package io.github.xiaoyu.javabasic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 生产者消费者 demo
 */
public class ProducerConsumerDemo {
    public static void main(String[] args) {
        LinkedBlockingDeque<Integer> q = new LinkedBlockingDeque<>();
        new Thread(new Producer(q)).start();
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q)).start();
    }

}

/**
 * io.github.xiaoyu.javabasic.Consumer
 */
class Consumer implements Runnable {

    /**
     * shared queue
     */
    private BlockingQueue<Integer> q;

    public Consumer(BlockingQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                Integer take = q.take();
                System.out.println("consumed: " + take);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

/**
 * producer
 */
class Producer implements Runnable {

    /**
     * shared queue
     */
    private BlockingQueue<Integer> q;

    public Producer(BlockingQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                q.put(i);
                System.out.println("produce: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
