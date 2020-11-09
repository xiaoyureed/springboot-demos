package io.github.xiaoyureed.rpc.server;

import io.github.xiaoyureed.rpc.common.HandlerRegistry;
import io.github.xiaoyureed.rpc.common.IMsgHandler;
import io.github.xiaoyureed.rpc.common.MsgIn;
import io.github.xiaoyureed.rpc.common.MsgRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
@Slf4j
public class MsgRealHandler extends ChannelInboundHandlerAdapter {
    private ThreadPoolExecutor executor;
    private MsgRegistry msgs;
    private HandlerRegistry handlers;

    public MsgRealHandler(MsgRegistry msgs,
                          HandlerRegistry handlers, int coreSize) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
        ThreadFactory factory = new ThreadFactory() {

            // 自定义 thread name
            final AtomicInteger seq = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("rpc-" + seq.getAndIncrement());
                return t;
            }

        };
        this.executor = new ThreadPoolExecutor(1, coreSize,
                30, TimeUnit.SECONDS, queue, factory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        this.handlers = handlers;
        this.msgs =  msgs;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MsgIn) {
            this.executor.execute(() -> {
                this.handleMessage(ctx, (MsgIn) msg);
            });
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MsgIn input) {
        // 业务逻辑在这里
        Class<?> clazz = msgs.get(input.getType());
        if (clazz == null) {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
            return;
        }
        Object o = input.parsePayload(clazz);
        IMsgHandler<Object> handler = (IMsgHandler<Object>) handlers.get(input.getType());
        if (handler != null) {
            handler.handle(ctx, input.getRequestId(), o);
        } else {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("connection error", cause);
    }

    public void closeGracefully() {
        this.executor.shutdown();
        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.executor.shutdownNow();
    }
}
