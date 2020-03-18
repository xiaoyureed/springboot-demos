package io.github.xiaoyu.java8demo.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * ref: https://github.com/biezhi/learn-java8/blob/master/java8-completablefuture/README.md
 *
 * CompletableFuture类实现了CompletionStage和Future接口, 作为future的拓展
 *
 * 为什么要拓展future? future获取最终结果的方法太少, 一个是通过 isDone 轮询, 调用get(), 一个是直接get(timeout)设置超时时间
 * 但是 get() 会阻塞thread, 显然不够好
 *
 * - 对结果进行变换, 接收一个 function - thenApply* 系列, 如果没有指定 executor, 会默认会在ForkJoinPool.commonPool()线程池中执行
 *
 * - 对结果进行消费, 接收一个 consumer - thenAccept* 系列, 类似的, 分是否 Async, 是否指定executor
 *
 * - 对上一步的计算结果不关心，执行下一个操作 , 接收一个 runnable - thenRun* 系列, 类似的
 *
 * - 两个future都完成时, 做出处理 - thenCombine*, thenAcceptBoth* 系列
 *      和 compose区别: compose的两个future是依赖关系
 *
 * - 两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的转化操作
 *          - applyEither* 系列
 *
 * - 计算完成后的处理 - whenComplete*
 *
 *      同时进行处理转换 - handle*
 */

/**
 * fluent api
 * <p>
 * exception handling
 *
 * @author xiaoyu
 * @date 2019/5/18
 */
public class CompletableFutureFluentApi {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calculate(50))
                .whenComplete((i, e) -> {
                    System.out.println("result = " + i);        // null
                    System.out.println("exception = " + e);     //    / by zero
                })
                .exceptionally(e -> {
                    System.out.println(e.getMessage());
                    return 0;                                   // 作为替代结果
                })
                .thenApply(s -> Integer.toString(s))
                .thenApply(s -> "\'" + s + "\'")
                .thenAccept(System.out::println)// '0'
                .thenRun(() -> System.out.println("finished!"));
        future.get();// return Void, aiming to block main thread
    }

    private static Integer calculate(Integer param) {
        int i = 2 / 0;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return param * param;
    }
}
