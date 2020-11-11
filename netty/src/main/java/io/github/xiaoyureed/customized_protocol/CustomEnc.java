package io.github.xiaoyureed.customized_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
public class CustomEnc extends MessageToByteEncoder<CustomProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          CustomProtocol msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent().getBytes());
    }
}
