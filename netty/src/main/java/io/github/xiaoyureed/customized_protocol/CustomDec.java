package io.github.xiaoyureed.customized_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
public class CustomDec extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf buf, List<Object> out) throws Exception {
        int len = buf.readInt();
        byte[] content = new byte[len];
        buf.readBytes(content);
        out.add(new CustomProtocol(len, new String(content)));
    }

    /**
     * 完善的写法
     */
    private void decode1(ChannelHandlerContext channelHandlerContext,
                         ByteBuf buf, List<Object> out) {
        // 长度太小, 不正常
        if (buf.readableBytes() < Consts.BASE_LEN) {
            return ;
        }
        // 长度太长, 可能是 socket 字节流攻击, 跳过这段报文
        if (buf.readableBytes() > 2048) {
            buf.skipBytes(buf.readableBytes());
        }

        // 数据报文起始位置
        int flag = 0;
        while (true) {
            flag = buf.readerIndex();
            // 标记起始位置
            buf.markReaderIndex();
            // 尝试读取 flag
            if (buf.readInt() == Consts.FLAT_START) {
                break;
            }
            // 如果没有找到 flag, 回复指针, 然后每次循环跳一个字节
            buf.resetReaderIndex();
            buf.readByte();
            // 当略过，一个字节之后, 重新检测数据包的长度,
            //如果不足长度, 应该结束。等待后面的数据到达
            if (buf.readableBytes() < Consts.BASE_LEN) {
                return;
            }
        }

        // 数据长度
        int len = buf.readInt();
        // 若数据还没有到齐
        if (buf.readableBytes() < len) {
            buf.readerIndex(flag);
            return;
        }

        byte[] content = new byte[len];
        buf.readBytes(content);
        out.add(new CustomProtocol(len, new String(content)));
    }
}
