package io.github.xiaoyureed.concurrentjava.other.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFutureDemo
 */
public class CompletableFutureDemo {
    // start until main thread complete the complex compute, then print result
    private static class AskTask implements Runnable {
        private CompletableFuture<Integer> future;

        public AskTask(CompletableFuture<Integer> future) {
            this.future = future;
        }

        @Override
        public void run() {
            try {
                Integer integer = future.get();// 阻塞, CompletableFuture为 "未完成" 状态
                System.out.println(integer);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // CompletableFuture<Integer> future = new CompletableFuture<Integer>();
        // new Thread(new AskTask(future)).start();// 启动等待线程

        // // simulate complex compute
        // Thread.sleep(1000);

        // // 告知运算结果: 100
        // future.complete(100);// 此时为 "完成"状态

        // CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() ->
        // calculate(20));
        // // supplyAsync() 用于需要有返回值的场景
        // // runAsync() 用于无需返回值的场景
        // System.out.println(supplyAsync.get());

        composeCompletableFutureDemo();
    }

    private static void composeCompletableFutureDemo() throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calculate(20))
                // .thenCompose(i -> CompletableFuture.supplyAsync(() -> calculate(i)))
                // 将上一个 future 的结果传入, 再次构造一个 future
                .thenCombine(CompletableFuture.supplyAsync(() -> calculate(10)), (i, j) -> {
                    System.out.println("i=" + i);
                    System.out.println("j=" + j);
                    return i + j;
                })
                .thenApply(i -> "\"" + i + "\"").thenAccept(System.out::println);// 结果: "160000"
                            // 结果:i=400 j=100 "500"

        future.get();
    }

    private static void fluentApiDemo() throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calculate(50))
                .exceptionally(e -> {           // 对 calculate 方法的异常处理
                    e.printStackTrace(); // java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
                    return 0; // 最终处理结果 : "0"
                })
                .thenApply(i -> Integer.toString(i)).thenApply(str -> "\"" + str + "\"")
                .thenAccept(System.out::println);// 结果: "2500"
        future.get(); // 需要让 main thread 阻塞, 等待 calculate执行完
        System.out.println(future.get());// 阻塞 // 结果: null
    }

    private static Integer calculate(Integer param) {
        try {
            // simulate complex calculation
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return param * param;
    }
}