package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server.bytebuf_issue_fix;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 解决 bytebuf 的问题 方案一 （不够好）
 *
 * @author xiaoyu
 * @since 1.0
 */
public class TimeClientHandlerNew extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;

    /**
     * channel handler 的声明周期方法
     * */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4); // (1)
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); // (1)
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m); // (2)all received data should be cumulated into buf
        m.release();

        //- check if buf has enough data
        //- Otherwise, Netty will call the channelRead() method again when more data arrives, and eventually
        // all 4 bytes will be cumulated.
        if (buf.readableBytes() >= 4) { // (3)
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
