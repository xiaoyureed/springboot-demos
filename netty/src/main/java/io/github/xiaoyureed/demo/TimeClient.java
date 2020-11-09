package io.github.xiaoyureed.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class TimeClient {
    private int port;
    private String ip;

    public TimeClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public static void main(String[] args) {

    }

    public void run() throws InterruptedException {
        // 是 boss 也是 worker
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    //used to create client-side channel
                    .channel(NioSocketChannel.class)
                    //we do not use childOption() here
                    //unlike we did with ServerBootstrap because the client-side
                    // SocketChannel does not have a parent
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new TimeClientHandler());
                            //ch.pipeline().addLast(new TimeClientHandlerNew());// 解决bytebuf方案一
                            // 结局 bytebuf 碎片化问题方案二
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // start the client, wait until connection opened
            // call the connect() method instead of the bind()
            ChannelFuture f = bootstrap.connect(ip, port).sync();
            //ChannelFuture connect = channelFuture.channel().connect(new InetSocketAddress("127.0.0.1", 6666));//一般使用启动器，这种方式不常用

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

/**
 * 这个 client handler 可能会抛出 IndexOutOfBoundsException
 *
 * 这是因为 在基于 stream 的传输中 （如  tcp/ip 传输）， 接受的数据被存储在 socket receive buffer 中， 但是 这个 buffer
 * 不是 数据包队列而是字节队列， 也就是说， 即使将两个消息作为两个单独的数据包发送， 接受到的也只是一坨字节而不是两条消息
 *
 * 最简单的解决方法是：创建一个 bytebuf， 直到4字节全部写入它， 才进行后续操作
 * see {@link TimeServerHandler#channelActive}
 *
 * @author xiaoyu
 * @since 1.0
 */
class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //In TCP/IP, Netty reads the data sent from a peer into a ByteBuf.
        ByteBuf byteBuf = (ByteBuf) msg; // (1)
        try {
            long currentTimeMillis = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

