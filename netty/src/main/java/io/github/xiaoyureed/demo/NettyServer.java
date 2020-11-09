package io.github.xiaoyureed.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Discards any incoming data.
 *
 * https://netty.io/wiki/user-guide-for-4.x.html
 *
 * 处理器Handler主要分为两种：
 *
 * ChannelInboundHandlerAdapter(入站处理器)、ChannelOutboundHandler(出站处理器)
 *
 * 入站指的是数据从底层java NIO Channel到Netty的Channel。
 *
 * 出站指的是通过Netty的Channel来操作底层的java NIO Channel。
 *
 * ChannelInboundHandlerAdapter处理器常用的事件有：
 *
 * 注册事件 fireChannelRegistered。
 * 连接建立事件 fireChannelActive。
 * 读事件和读完成事件 fireChannelRead、fireChannelReadComplete。
 * 异常通知事件 fireExceptionCaught。
 * 用户自定义事件 fireUserEventTriggered。
 * Channel 可写状态变化事件 fireChannelWritabilityChanged。
 * 连接关闭事件 fireChannelInactive。
 *
 *
 * ChannelOutboundHandler处理器常用的事件有：
 *
 * 端口绑定 bind。
 * 连接服务端 connect。
 * 写事件 write。
 * 刷新时间 flush。
 * 读事件 read。
 * 主动断开连接 disconnect。
 * 关闭 channel 事件 close。
 *
 *
 * 还有一个类似的handler()，主要用于装配parent通道，也就是bossGroup线程。一般情况下，都用不上这个方法。
 *
 * 获取 channel 状态:
 *
 * boolean isOpen(); //如果通道打开，则返回true
 * boolean isRegistered();//如果通道注册到EventLoop，则返回true
 * boolean isActive();//如果通道处于活动状态并且已连接，则返回true
 * boolean isWritable();//当且仅当I/O线程将立即执行请求的写入操作时，返回true。
 *
 * @author xiaoyu
 * @since 1.0
 */
@Data
public class NettyServer {
    private int port;

    public void run() throws Exception {
        // 两个线程组
        //bossGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中。
        //workerGroup用于处理每一个连接发生的读写事件。
        //假设想自定义线程数，可以使用有参构造器
        EventLoopGroup bossGroup   = new NioEventLoopGroup(); // (1) accepts an incoming connection.
        EventLoopGroup workerGroup = new NioEventLoopGroup();//handles the traffic of the accepted connection
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)helper class that sets up a server.
            b.group(bossGroup, workerGroup)
                    //instantiate a new server side Channel
                    //to accept incoming connections.
                    .channel(NioServerSocketChannel.class)
                    //The ChannelInitializer is a special handler that is purposed to
                    // help a user configure a new Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // setup the handler for channel
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ////add more handlers to the pipeline
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    // specify params for the Channel
                    // ref: https://netty.io/4.1/api/io/netty/channel/ChannelOption.html
                    //
                    // childoptions:
                    //SO_RCVBUF Socket参数，TCP数据接收缓冲区大小。
                    //TCP_NODELAY TCP参数，立即发送数据，默认值为Ture。
                    //SO_KEEPALIVE Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。
                    //
                    // options:
                    //SO_BACKLOG Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128。
                    //
                    //for the NioServerSocketChannel that accepts incoming connections.也就是boosGroup线程
                    ////设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //for the Channels accepted by the parent ServerChannel,也就是workerGroup线程。
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            // bind() 可被调用多次绑定不同地址
            // netty 中的操作都是异步的, 这里 使用 sync() 进行同步等待
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            // close event loop
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 测试 telnet localhost 8080
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        NettyServer server = new NettyServer();
        server.setPort(port);
        server.run();
        System.out.println(">>> server listen on the port: " + port);
    }
}

class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf in = (ByteBuf) msg;
            while (in.isReadable()) {
                System.out.println(">>> " + (char) in.readByte());
                System.out.flush();
            }
//              System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        } finally {
            ReferenceCountUtil.release(msg);
        }

        ////获取ChannelPipeline对象
        //ChannelPipeline pipeline = ctx.channel().pipeline();
        ////往pipeline中添加ChannelHandler处理器，装配流水线
        //pipeline.addLast(new MyServerHandler());
    }
}

class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * - netty 屏蔽了 Java nio 中的 flip(), because it has two pointers; one for read operations and the other
     * for write operations. The writer index increases when you write something to a ByteBuf while the reader
     * index does not change. The reader index and the writer index represents where the message starts and ends .
     *
     * - A ChannelFuture represents an I/O operation which has not yet occurred. It means, any requested
     * operation might not have been performed yet because all operations are asynchronous in Netty.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);// 这一步会自动 release msg
        ctx.flush();
        //ctx.writeAndFlush(msg)
    }
}

class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接被建立后就触发
     *
     * 回写一个数字, 之后关闭 server
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        // 4 bytes
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            // 等待 msg 发送完之后触发
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;

                //判断是否操作成功
                if (future.isSuccess()) {
                    System.out.println("连接成功");
                } else {
                    System.out.println("连接失败");
                }

                ctx.close();
            }
        }); // (4)
        //f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

/**
 * 长时间的操作交给taskQueue异步处理
 */
class LongTimeTaskHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //长时间操作，不至于长时间的业务操作导致Handler阻塞
                    Thread.sleep(1000);
                    System.out.println("长时间的业务处理");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

/**
 * scheduleTaskQueue延时任务队列
 */
class ScheduleTaskHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().schedule(() -> {
            try {
                //长时间操作，不至于长时间的业务操作导致Handler阻塞
                Thread.sleep(1000);
                System.out.println("长时间的业务处理");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);// 延时操作
    }
}

