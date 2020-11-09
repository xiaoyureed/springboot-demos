package io.github.xiaoyureed.rpc.server;

import io.github.xiaoyureed.rpc.common.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * https://zhuanlan.zhihu.com/p/35720383
 * https://sourcegraph.com/github.com/pyloque/rpckids@master/-/blob/src/main/java/rpckids/client/RPCClient.java
 * https://github.com/pyloque/httpkids
 *
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
@Slf4j
public class RpcServer {
    private final String ip;
    private final int port;
    private final int ioThreads;
    private final int bizThreads;

    private final HandlerRegistry handlers;
    private final MsgRegistry msgs;

    public RpcServer(String ip, int port, int ioThreads, int bizThreads) {
        this.ip = ip;
        this.port = port;
        this.ioThreads = ioThreads;
        this.bizThreads = bizThreads;

        handlers = new HandlerRegistry();
        handlers.defaultHandler(new IMsgHandler<MsgIn>() {
            @Override
            public void handle(ChannelHandlerContext ctx, String requestId, MsgIn input) {
                log.error("unrecognized message type {} comes", input.getType());
                ctx.close();
            }
        });

        msgs = new MsgRegistry();
    }

    public RpcServer register(String type, Class<?> msgClass, IMsgHandler<?> handler) {
        handlers.register(type, handler);
        msgs.register(type, msgClass);
        return this;
    }

    public void start() {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(ioThreads);
        MsgRealHandler msgRealHandler = new MsgRealHandler(msgs, handlers, bizThreads);
        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(eventExecutors)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ReadTimeoutHandler(60),
                                    new MsgDecoder(), new MsgEncoder(), msgRealHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = boot.bind(ip, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
            // stop our own thread group
            msgRealHandler.closeGracefully();
        }

    }
}
