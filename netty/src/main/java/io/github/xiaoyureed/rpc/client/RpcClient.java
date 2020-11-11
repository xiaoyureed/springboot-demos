package io.github.xiaoyureed.rpc.client;

import io.github.xiaoyureed.rpc.common.MsgDecoder;
import io.github.xiaoyureed.rpc.common.MsgEncoder;
import io.github.xiaoyureed.rpc.common.MsgRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/10
 */
@Slf4j
public class RpcClient {
    private String ip;
    private int port;
    private int ioThreads;
    private int timeoutSeconds;
    private MsgReadHandlerClientSide handler;
    private MsgRegistry msgRegistry;

    private RpcClient me() {
        return this;
    }

    public void start() {
        Bootstrap boot = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(ioThreads);
        try {
            boot.channel(NioSocketChannel.class).group(group)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new ReadTimeoutHandler(timeoutSeconds),
                                    new MsgDecoder(), new MsgEncoder(), new MsgReadHandlerClientSide(msgRegistry, me()));

                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = boot.connect(ip, port).sync();
            log.debug(">>> nio client start success");
            f.channel().closeFuture().sync();
            log.debug(">>> nio client close success");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void reconnect() {

    }
}
