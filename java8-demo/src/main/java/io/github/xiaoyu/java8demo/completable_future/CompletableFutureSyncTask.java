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
public class CompletableFutureSyncTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务, 直接开始
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> complexTask(20));

        // 此时可以 do sth else ...
        System.out.println("do sth else...");

        Integer result = future.get();// block
        System.out.println("result = " + result);
    }

    private static Integer complexTask(Integer param) {
        // simulate complex task
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return param * param;
    }
}
