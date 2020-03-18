package io.github.xiaoyureed.concurrentjava.netty.demo.discard_server;

import io.github.xiaoyureed.concurrentjava.netty.demo.discard_server.bytebuf_issue_fix.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                    .channel(NioSocketChannel.class) //used to create client-side channel
                    //we do not use childOption() here unlike we did with ServerBootstrap because the client-side
                    // SocketChannel does not have a parent
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new TimeClientHandler());
                            //ch.pipeline().addLast(new TimeClientHandlerNew());// 解决bytebuf方案一
                            // 结局 bytebuf 碎片化问题方案二
                            ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());

                        }
                    });

            // start client, wait until connection opened
            ChannelFuture f = bootstrap.connect(ip, port).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
