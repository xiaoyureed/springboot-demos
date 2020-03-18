package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel
 *
 * @author xiaoyu
 * @since 1.0
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);

        cause.printStackTrace();
        ctx.close();
    }

    /**
     * will be invoked when a connection is established and ready to generate traffic
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);

        time(ctx);
    }

    /**
     * function: send msg without receiving any requests and closes the connection once the message is sent.
     */
    private void time(ChannelHandlerContext ctx) {
        //To send a new message, we need to allocate a new buffer, 32bit 只需要4字节
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        // write to buffer
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));// 将时间转为 32bit
        System.out.println(">>> write to buffer: " + System.currentTimeMillis() / 1000L + 2208988800L);

        // - netty 屏蔽了 Java nio 中的 flip(), because it has two pointers; one for read operations and the other
        // for write operations. The writer index increases when you write something to a ByteBuf while the reader
        // index does not change. The reader index and the writer index represents where the message starts and ends .
        //
        // - A ChannelFuture represents an I/O operation which has not yet occurred. It means, any requested
        // operation might not have been performed yet because all operations are asynchronous in Netty.
        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                //call the close() method after the ChannelFuture is complete
                // 而且 close() 也是异步
                ctx.close();
            }
        }); // (4)
        // <=>
        //f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);

        //discard(ctx, msg);
        //print(ctx, msg);
        echo(ctx, msg);
    }

    private void discard(ChannelHandlerContext ctx, Object msg) {
        try {
            // do nothing

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void print(ChannelHandlerContext ctx , Object msg) {
        try {
            ByteBuf in = (ByteBuf) msg; // "in" is a bytebuf -  reference-counted object
            //while (in.isReadable()) {
            //    System.out.println(">>> from client: " + (char) in.readByte());// \r\n 也被传过来了
            //    System.out.flush();
            //}

            // <=>
            System.out.println(in.toString(CharsetUtil.UTF_8));

        } finally {
            ReferenceCountUtil.release(msg);// in.release()
        }
    }

    private void echo(ChannelHandlerContext ctx, Object msg) {
        // ChannelHandlerContext 提供各种操作用来触发 I/O events
        // write to buffer
        ctx.write(msg);// 这一步会自动 release msg
        //System.out.println(">>> buffered msg to client: " + msg);

        //flushed out to the wire
        ctx.flush();
        // <=>
        //ctx.writeAndFlush(msg)
    }

}
