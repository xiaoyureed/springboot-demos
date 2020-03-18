package io.github.xiaoyureed.concurrentjava.other.producer_consumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Producer
 */
public class Producer implements Runnable {

  private volatile boolean running = true;
  private BlockingQueue<Data> queue;
  private AtomicInteger count = new AtomicInteger(0);

  public Producer(BlockingQueue<Data> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    System.out.println("start [producer], id=" + Thread.currentThread().getId());

    try {
      while (running) {
        // 构造数据
        Thread.sleep(new Random().nextInt(1000));
        Data data = new Data(count.incrementAndGet());
  
        boolean ok = queue.offer(data, 2, TimeUnit.SECONDS);
        if (!ok) {
          System.err.println("failed to put data, data: " + data);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }

  }

  public void stop() {
    this.running = false;
  }
}