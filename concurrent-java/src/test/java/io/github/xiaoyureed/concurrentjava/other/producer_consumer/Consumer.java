package io.github.xiaoyureed.concurrentjava.other.producer_consumer;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Consumer
 */
public class Consumer implements Runnable {

  private BlockingQueue<Data> queue;

  public Consumer(BlockingQueue<Data> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    System.out.println("start [consumer], id=" + Thread.currentThread().getId());

    try {
      while (true) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("stop [consumer], id=" + Thread.currentThread().getId());
          break;
        }

        Data data = queue.take();
        if (null != data) {
          int re = data.getIntDdata() * data.getIntDdata();
          Thread.sleep(new Random().nextInt(1000));// simulate the time consumption

          System.out.println(MessageFormat.format("{0} * {1} = {2}", data.getIntDdata(), data.getIntDdata(), re));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();// 中断
    }
  }
}