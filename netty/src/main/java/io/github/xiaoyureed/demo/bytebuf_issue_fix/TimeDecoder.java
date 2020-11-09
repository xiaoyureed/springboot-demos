package io.github.xiaoyureed.demo.bytebuf_issue_fix;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 添加到 channel 配置中 ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
 * @author xiaoyu
 * @since 1.0
 */
public class TimeDecoder extends ByteToMessageDecoder { // (1)
    /**
     * ByteToMessageDecoder calls the decode() method with an internally maintained cumulative buffer whenever
     * new data is received
     * */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        //- decode() can decide to add nothing to out where there is not enough data in the cumulative buffer.
        //- will call decode() again when there is more data received.
        if (in.readableBytes() < 4) {
            return; // (3)
        }

        //- If decode() adds an object to out, it means the decoder decoded a message successfully
        //- you don't need to decode multiple messages. ByteToMessageDecoder will keep calling the decode() method
        // until it adds nothing to out.
        out.add(in.readBytes(4)); // (4)
    }
}
