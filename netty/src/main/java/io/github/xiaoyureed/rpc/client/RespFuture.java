package io.github.xiaoyureed.rpc.client;

import java.util.concurrent.*;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
public class RespFuture<T> implements Future<T> {

    private T result;

    private Throwable throwable;

    private CountDownLatch countDownLatch;

    public RespFuture() {
        this.countDownLatch = new CountDownLatch(1);
    }

    public void ok(T result) {
        this.result = result;
        countDownLatch.countDown();
    }

    public void error(Throwable throwable) {
        this.throwable = throwable;
        countDownLatch.countDown();
    }

    /**
     * 决定是否取消任务
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * 是否已经取消
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * 是否已经完成
     */
    @Override
    public boolean isDone() {
        return this.result != null || this.throwable != null;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
        return result;
    }
}
