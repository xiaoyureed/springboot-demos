package io.github.xiaoyu.java8demo.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 多个 future 一起工作
 * @author xiaoyu
 * @date 2019/5/18
 */
public class CompletableFutureMulti {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // combineFuture();
        // composeFuture();
        acceptBoth();
    }

    /**
     * compose - 组成, 俩future是依赖关系
     */
    private static  void composeFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> compute(2))
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> compute(i)))
                // 等待上一个future得到结果, 然后将上一个 future 的
                // 结果传入(这俩 future 是依赖关系), 再次构造一个 future
                .thenApply(i -> "\"" + i + "\"")
                .thenAccept(System.out::println);// 结果: "16"
        future.get();
    }

    /**
     * combine - 合作, 俩future是独立的
     */
    private static void combineFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> compute(20))
                // 首先完成前一个 future  和 跟着的 future (这俩future 没有任何关系), 然后将两者结果进行计算
                .thenCombine(CompletableFuture.supplyAsync(() -> compute(10)), (i, j) -> {
                    System.out.println("i=" + i);
                    System.out.println("j=" + j);
                    return i + j;
                })
                .thenApply(i -> "\"" + i + "\"")
                .thenAccept(System.out::println);
        // 结果:i=400 j=100 "500"

        future.get();
    }

    private static void acceptBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> 233)
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> "hello"), (a, b) -> {
                    System.out.println(a);
                    System.out.println(b);
                });
        future.get();
    }

    private static Integer compute(Integer param) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return param * param;
    }
}
