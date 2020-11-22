package io.github.xiaoyureed.springboot_heart_beat.server;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
@Component
@Profile("server")
@Slf4j
public class HeartbeatServer {

    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    @Value("${netty.server.port:9999}")
    private int port;

    @Autowired
    private HeartbeatProto pong;
    //     private static final ByteBuf HEART_BEAT =  Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new CustomProtocol(123456L,"pong").toString(),CharsetUtil.UTF_8));

    private static final int READER_IDLE_TIME_SECONDS = 5;

    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap boot = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                //
                                new IdleStateHandler(READER_IDLE_TIME_SECONDS, 0, 0),
                                new HeartbeatDec(),
                                new SimpleChannelInboundHandler<HeartbeatProto>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatProto heartbeatProto) throws Exception {
                                        try {
                                            log.info(">>> server: receive heartbeat {}", heartbeatProto.toString());
                                            //保存客户端与 Channel 之间的关系
                                            SocketChannelHolder.put(heartbeatProto.getId(), (NioSocketChannel) ctx.channel());
                                        } finally {
                                            ReferenceCountUtil.release(heartbeatProto);
                                        }
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        // 取消绑定
                                        SocketChannelHolder.remove((NioSocketChannel) ctx.channel());
                                    }

                                    @Override
                                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                        if (evt instanceof IdleStateEvent) {
                                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                                            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                                                log.info(">>> server: 已经 {}s 没有收到消息了", READER_IDLE_TIME_SECONDS);
                                                log.info(">>> server: 发送心跳");
                                                ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(pong.toString(), StandardCharsets.UTF_8));
                                                ctx.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                                            }
                                            return;
                                        }
                                        super.userEventTriggered(ctx, evt);
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        log.error(">>> cause: {}", cause.getMessage());
                                        cause.printStackTrace();
                                        ctx.close();
                                    }
                                }
                        );
                    }
                })
                .localAddress(new InetSocketAddress(port))
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture f = boot.bind().sync();
        if (f.isSuccess()) {
            log.info(">>> server: start ok");
        }
//        f.channel().closeFuture().sync();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info(">>> server: stop success");
    }
}
