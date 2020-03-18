package io.github.xiaoyu.java8demo.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步任务, future作为异步任务的执行者
 *
 * // supplyAsync() 用于需要有返回值的场景
 *
 * // runAsync() 用于无需返回值的场景
 *
 * @author xiaoyu
 * @date 2019/5/18
 */
public class CompletableFutureSyncTask_1 {
    public static void main(String[] args) {
        CompletableFuture<Void> futureWithoutResult = CompletableFuture.runAsync(() -> System.out.println("hello completable future"));
        CompletableFuture<Void> futureWithResult    = CompletableFuture.supplyAsync(() -> 233).thenAccept(System.out::println);
    }

}
