package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server.use_pojo;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        time(ctx);
    }

    private void time(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new UnixTime()).addListener(ChannelFutureListener.CLOSE);
    }
}
