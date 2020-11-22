package java8.producer_consumer;

import java.util.concurrent.*;

/**
 * Client
 */
public class Client {

  public static void main(String[] args) {
    LinkedBlockingQueue<Data> queue = new LinkedBlockingQueue<Data>();

    Consumer consumer1 = new Consumer(queue);
    Consumer consumer2 = new Consumer(queue);
    Consumer consumer3 = new Consumer(queue);

    Producer producer1 = new Producer(queue);
    Producer producer2 = new Producer(queue);
    Producer producer3 = new Producer(queue);

    ExecutorService pool = new ThreadPoolExecutor(6, 6, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());
    pool.execute(producer1);
    pool.execute(producer2);
    pool.execute(producer3);
    pool.execute(consumer1);
    pool.execute(consumer2);
    pool.execute(consumer3);

    try {
      Thread.sleep(10*1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    producer1.stop();
    producer2.stop();
    producer3.stop();

    try {
      Thread.sleep(3*1000);
    } catch (Exception e) {
      e.printStackTrace();
    }

    pool.shutdown();
  }
}