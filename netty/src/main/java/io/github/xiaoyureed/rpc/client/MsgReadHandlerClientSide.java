package io.github.xiaoyureed.rpc.client;

import io.github.xiaoyureed.rpc.common.MsgIn;
import io.github.xiaoyureed.rpc.common.MsgOut;
import io.github.xiaoyureed.rpc.common.MsgRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
@Slf4j
public class MsgReadHandlerClientSide extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;
    private MsgRegistry msgRegistry;
    private ConcurrentMap<String, RespFuture<?>> unfinishedTasks;
    private RpcClient client;

    private static final Exception CONN_CLOSED = new Exception(">>> rpc connection not active error");

    public MsgReadHandlerClientSide(MsgRegistry msgRegistry, RpcClient client) {
        this.msgRegistry = msgRegistry;
        this.client = client;
        this.unfinishedTasks = new ConcurrentHashMap<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof MsgIn)) {
            return;
        }
        MsgIn input = (MsgIn) msg;
        Class<?> clazz = msgRegistry.get(input.getType());
        if (clazz == null) {
            log.error(">>> unrecognized msg type {}", input.getType());
            return;
        }
        Object parsedPayload = input.getParsedPayload(clazz);
        RespFuture<Object> respFuture = (RespFuture<Object>) unfinishedTasks.remove(input.getRequestId());
        if (respFuture == null) {
            log.error("future not found with type {}", input.getType());
            return;
        }
        respFuture.ok(parsedPayload);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.context = null;
        this.unfinishedTasks.forEach((reqId, task) -> {
            task.error(CONN_CLOSED);
        });
        this.unfinishedTasks.clear();

        // 重连
        ctx.channel().eventLoop().schedule(() -> client.reconnect(), 1, TimeUnit.SECONDS);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        this.context = context;
    }

    public <T> RespFuture<T> send(MsgOut msg) {
        RespFuture<T> resp = new RespFuture<>();
        if (context == null) {
            resp.error(CONN_CLOSED);
        }
        this.context.channel().eventLoop().execute(() -> {
            context.writeAndFlush(msg);
            unfinishedTasks.put(msg.getRequestId(), resp);
        });
        return resp;
    }
}
