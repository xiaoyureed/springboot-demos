package io.github.xiaoyureed.rpc.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
@FunctionalInterface
public interface IMsgHandler<T> {
    /**
     * 消息处理器接口
     */
    void handle(ChannelHandlerContext ctx, String requestId, T message);
}
