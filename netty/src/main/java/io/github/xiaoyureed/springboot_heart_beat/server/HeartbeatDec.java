package io.github.xiaoyureed.springboot_heart_beat.server;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
public class HeartbeatDec extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        long id = byteBuf.readLong();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        list.add(new HeartbeatProto(id, new String(bytes)));
    }
}
