package io.github.xiaoyu.javabasic;

/**
 * 用volatile修饰的变量，表示该变量不稳定, 常变化
 *
 * 线程在每次使用变量的时候，都会重新读取变量的值
 *
 * 无法保证线程安全
 *
 * @author xiaoyu
 * @since 1.0
 */
public class VolatileDemo {
    //private int count;
    private volatile int count = 0;// 变量使用 volatile 无法保证 count==50

    private void inc() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileDemo demo = new VolatileDemo();
        Runnable r = () -> {
            for (int i = 0; i < 1000; i++) {
                demo.inc();
            }
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(r);
            threads[i] = thread;
            thread.start();
        }
        for (;;) {
            if (checkAllThreadDie(threads))
                break;
        }
        System.out.println("count: " + demo.count);// 最终结果达不到10000
    }

    private static boolean checkAllThreadDie(Thread[] threads) {
        for (Thread t: threads) {
            if (t.isAlive()) {
                return false;
            }
        }
        return true;
    }

}