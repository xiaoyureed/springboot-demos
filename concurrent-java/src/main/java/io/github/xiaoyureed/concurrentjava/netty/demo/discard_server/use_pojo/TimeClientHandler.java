package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server.use_pojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 这个 client handler 可能会抛出 IndexOutOfBoundsException
 *
 * 这是因为 在基于 stream 的传输中 （如  tcp/ip 传输）， 接受的数据被存储在 socket receive buffer 中， 但是 这个 buffer
 * 不是 数据包队列而是字节队列， 也就是说， 即使将两个消息作为两个单独的数据包发送， 接受到的也只是一坨字节而不是两条消息
 *
 * A 32-bit integer is a very small amount of data, and it is not likely to be fragmented often. However, the problem
 * is that it can be fragmented, and the possibility of fragmentation will increase as the traffic increases.
 *
 * 最简单的解决方法是：创建一个 bytebuf， 直到4字节全部写入它， 才进行后续操作
 *
 * @author xiaoyu
 * @since 1.0
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime time = (UnixTime) msg;
        System.out.println(">>> time:" + time);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
