package io.github.xiaoyureed.springboot_heart_beat.client;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
public class HeartbeatEnc extends MessageToByteEncoder<HeartbeatProto> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          HeartbeatProto heartbeatProto, ByteBuf byteBuf) throws Exception {
        byteBuf.writeLong(heartbeatProto.getId());
        byteBuf.writeBytes(heartbeatProto.getContent().getBytes());
    }
}
