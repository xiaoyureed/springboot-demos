package java8;

import java.util.concurrent.*;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/13
 */
public class FutureDemo {
    public static void main(String[] args) throws Exception{
        t1();
    }

    private static void t1() throws ExecutionException, InterruptedException {
        ExecutorService group = Executors.newSingleThreadExecutor();
        Future<String> future = group.submit(() -> {
            Thread.sleep(2000);
            return "result";
        });

        // block for 2s
        String result = future.get();
        System.out.println(result);

        group.shutdown();
    }

    private static void testFutureTask() throws ExecutionException, InterruptedException {
        //FutureTask既可以当做Runnable也可以当做Future。线程通过执行FutureTask的run方法，
        //将正常运行的结果放入FutureTask类的result变量中。使用get方法可以阻塞直到获得结果

        ExecutorService group = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        FutureTask<String> stringFutureTask = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(2);
            return "result";
        });
        group.submit(stringFutureTask);
        String result = stringFutureTask.get();
        System.out.println(result);
        group.shutdown();


    }
}
