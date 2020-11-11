package io.github.xiaoyureed.customized_protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
public class Client {

    public static void main(String[] args) {
        new Client().start("localhost", 8080);
    }

    public void start(String ip, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap boot = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(
                            // 编码器, 出站处理
                            new CustomEnc(),
                            // 处理器, 入站处理
                            new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                    System.out.println(">>> resp: " + byteBuf.toString(StandardCharsets.UTF_8));
                                }
                            });
                }
            });
            ChannelFuture f = boot.connect(ip, port).sync();
            Channel channel = f.channel();

            channel.writeAndFlush(new CustomProtocol("hello custom proto"));

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
