package io.github.xiaoyureed.rpc.common;

import io.github.xiaoyureed.rpc.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
public class MsgEncoder extends MessageToMessageEncoder<MsgOut> {
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          MsgOut msg, List<Object> out) throws Exception {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        writeStr(buf, msg.getRequestId());
        writeStr(buf, msg.getType());
        writeStr(buf, JsonUtils.toJson(msg.getPayload()));
        out.add(buf);
    }

    private void writeStr(ByteBuf buf, String s) {
        buf.writeInt(s.length());
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }
}
