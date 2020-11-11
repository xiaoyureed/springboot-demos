package io.github.xiaoyureed.customized_protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
public class Server {

    public static void main(String[] args) {
        new Server().start( 8080);
    }

    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap boot = new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    // 解码器, 入站处理
                                    new CustomDec(),
                                    // 处理器,
                                    // 发送完成, 默认就 关闭和 client 的连接,  ctx.close()
                                    //等价 .addListener(ChannelFutureListener.CLOSE)
                                    new SimpleChannelInboundHandler<CustomProtocol>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext context, CustomProtocol customProtocol) throws Exception {
                                            System.out.println(">>> server get: " + customProtocol);
                                            context.channel().writeAndFlush(Unpooled.copiedBuffer(">>> server get", StandardCharsets.UTF_8))
                                                    .addListener(ChannelFutureListener.CLOSE);
                                        }
                                    });
                        }
                    });
            ChannelFuture f = boot.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
