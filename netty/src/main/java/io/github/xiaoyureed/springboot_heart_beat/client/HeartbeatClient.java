package io.github.xiaoyureed.springboot_heart_beat.client;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
import java.nio.charset.StandardCharsets;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
@Component
@Profile({"client0", "client1"})
@Slf4j
public class HeartbeatClient {
    @Value("${netty.server.port:9999}")
    private int serverPort;
    @Value("${netty.server.host:127.0.0.1}")
    private String serverHost;

    private final NioEventLoopGroup group = new NioEventLoopGroup();
    private SocketChannel socketChannel;

    @Autowired
    private HeartbeatProto heartbeatProto;

    private static final int WRITER_IDLE_TIME_SECONDS = 10;

    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap boot = new Bootstrap().group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                // 每隔十秒检测是否发送过消息, 没发送消息触发 idleStateEvent 事件
                                new IdleStateHandler(0, WRITER_IDLE_TIME_SECONDS, 0),
                                new HeartbeatEnc(),
                                new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                        try {
                                            log.info(">>> client: receive heartbeat {}", byteBuf.toString(StandardCharsets.UTF_8));
                                        } finally {
                                            ReferenceCountUtil.release(byteBuf);
                                        }
                                    }

                                    /**
                                     * 用户事件触发器
                                     */
                                    @Override
                                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                        if (evt instanceof IdleStateEvent) {
                                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                                            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                                                log.info(">>> client: 已经 {} s 没有发送消息了", WRITER_IDLE_TIME_SECONDS);
                                                log.info(">>> client: 发送心跳");
                                                ctx.writeAndFlush(heartbeatProto).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                                            }
                                        }
                                        super.userEventTriggered(ctx, evt);
                                    }
                                }
                        );
                    }
                });
        ChannelFuture f = boot.connect(serverHost, serverPort).sync();
        if (f.isSuccess()) {
            log.info(">>> client: start ok");
        }
        socketChannel = (SocketChannel) f.channel();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        socketChannel.closeFuture().sync();
        group.shutdownGracefully();
        log.info(">>> client: stop ok");
    }
}
