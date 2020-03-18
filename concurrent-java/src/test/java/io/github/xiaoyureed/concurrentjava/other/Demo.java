package io.github.xiaoyureed.concurrentjava.other;

public class Demo {
    // private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        // int v1 = 1023434540;
        // int v2 = 1448547380;
        // int v3 = (v1 + v2) / 2;
        // System.out.println("v3: " + v3);// v3: -911492688

        // new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
        // workQueue)

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    //TODO: handle exception
                }
                System.out.println("I am side thread");
            }
        }.start();

        System.out.println("I am main thread");
    }
}