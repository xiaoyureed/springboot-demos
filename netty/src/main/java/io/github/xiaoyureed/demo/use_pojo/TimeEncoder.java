package io.github.xiaoyureed.demo.use_pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * UnixTime to byteBuf
 *
 * @author xiaoyu
 * @since 1.0
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m       = (UnixTime) msg;
        ByteBuf  encodedBuf = ctx.alloc().buffer(4);
        encodedBuf.writeInt((int)m.value());

        //-First, we pass the original ChannelPromise as-is so that Netty marks it as success or failure when the
        // encoded data is actually written out to the wire.
        //- Second, we did not call ctx.flush(). There is a separate handler method
        // `void flush(ChannelHandlerContext ctx)` which is purposed to override the flush() operation.
        ctx.write(encodedBuf, promise); // (1)
    }
}

// 更简单方式
//public class TimeEncoder extends MessageToByteEncoder<UnixTime> {
//    @Override
//    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
//        out.writeInt((int)msg.value());
//    }
//}
