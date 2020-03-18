package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Data
public class DiscardServer {
    private int port;

    public void run() throws Exception {
        EventLoopGroup bossGroup   = new NioEventLoopGroup(); // (1) accepts an incoming connection.
        EventLoopGroup workerGroup = new NioEventLoopGroup();//handles the traffic of the accepted connection
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)helper class that sets up a server.
            b.group(bossGroup, workerGroup)
                    //specify to use the NioServerSocketChannel class which is used to instantiate a new Channel to
                    // accept incoming connections.
                    .channel(NioServerSocketChannel.class) // (3)
                    //The ChannelInitializer is a special handler that is purposed to
                    // help a user configure a new Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());//add more handlers to the pipeline
                        }
                    })
                    //for the NioServerSocketChannel that accepts incoming connections.
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //for the Channels accepted by the parent ServerChannel
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            // bind() 可被调用多次绑定不同地址
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9527;
        DiscardServer server = new DiscardServer();
        server.setPort(port);
        server.run();
        System.out.println(">>> server listen on the port: " + port);
    }
}
