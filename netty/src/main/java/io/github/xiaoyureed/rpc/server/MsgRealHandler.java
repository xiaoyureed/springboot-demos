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
    private final ThreadPoolExecutor executor;
    private final MsgRegistry msgs;
    private final HandlerRegistry handlers;

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
        // 闲置时间超过30秒的线程自动销毁
        this.executor = new ThreadPoolExecutor(1, coreSize,
                30, TimeUnit.SECONDS, queue, factory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        this.handlers = handlers;
        this.msgs =  msgs;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug(">>> connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug(">>> connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug(">>> channel read, begin processing...");
        if (msg instanceof MsgIn) {
            this.executor.execute(() -> {
                this.handleMessage(ctx, (MsgIn) msg);
            });
        } else {
            log.warn(">>> msg type error, should be {}, but get {}",
                    MsgIn.class.getName(), msg.getClass().getName());
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MsgIn input) {
        Class<?> clazz = msgs.get(input.getType());
        // // 没注册的消息用默认的处理器处理
        if (clazz == null) {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
            return;
        }
        Object o = input.getParsedPayload(clazz);
        IMsgHandler<Object> handler = (IMsgHandler<Object>) handlers.get(input.getType());
        if (handler != null) {
            handler.handle(ctx, input.getRequestId(), o);
        } else {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(">>> connection error", cause);
        cause.printStackTrace();
//        ctx.close();
    }

    public void closeGracefully() {
        // 先通知关闭
        this.executor.shutdown();
        // 等待 10s
        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 强制关闭
        this.executor.shutdownNow();
    }
}
