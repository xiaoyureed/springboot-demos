package io.github.xiaoyu.java8demo.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 完成了就通知我, 一种任务通知机制, future作为一个listener
 *
 * 复杂的计算任务在 main thread
 *
 * @author xiaoyu
 * @date 2019/5/18
 */
public class CompletableFutureListener {

    private static class AskTask implements Runnable {
        private CompletableFuture<Integer> future;

        public AskTask(CompletableFuture<Integer> future) {
            this.future = future;
        }

        @Override
        public void run() {
            Integer result = null;
            try {
                result = future.get(); // block until future.complete()
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("result = " + result);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new AskTask(future)).start();

        // simulate complex task
        Thread.sleep(1000);

        future.complete(100);// 通知 // tell the future result
    }
}
