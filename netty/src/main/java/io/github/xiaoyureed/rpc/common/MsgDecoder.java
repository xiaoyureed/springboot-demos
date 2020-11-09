package io.github.xiaoyureed.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
public class MsgDecoder extends ReplayingDecoder<MsgIn> {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {
        String requestId = readStr(in);
        String type = readStr(in);
        String content = readStr(in);
        out.add(new MsgIn(type, requestId, content));
    }

    private String readStr(ByteBuf in) {
        //// 字符串先长度后字节数组，统一UTF8编码
        int len = in.readInt();
        if (len < 0 || len > (1 << 20)) {
            throw new DecoderException("string too long len=" + len);
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
