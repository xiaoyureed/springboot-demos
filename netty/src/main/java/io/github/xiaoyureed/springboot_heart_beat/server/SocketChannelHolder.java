package io.github.xiaoyureed.springboot_heart_beat.server;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 当有多个客户端连上来时，服务端需要区分不同 client 对应的 channel
 * 所以每个 client 的 reqId 必须唯一
 *
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
public class SocketChannelHolder {
    private static final ConcurrentMap<Long, NioSocketChannel> REQID_CHANNEL_MAPPING;

    static {
        REQID_CHANNEL_MAPPING = new ConcurrentHashMap<>();
    }

    public static void put(Long id, NioSocketChannel channel) {
        REQID_CHANNEL_MAPPING.put(id, channel);
    }

    public static NioSocketChannel get(Long id) {
        return REQID_CHANNEL_MAPPING.get(id);
    }

    public static void remove(NioSocketChannel channel) {
        REQID_CHANNEL_MAPPING.entrySet().stream()
                .filter(entry -> entry.getValue() == channel)
                .forEach(entry -> REQID_CHANNEL_MAPPING.remove(entry.getKey()));
    }

    public static Map<Long, NioSocketChannel> map() {
        return REQID_CHANNEL_MAPPING;
    }
}
