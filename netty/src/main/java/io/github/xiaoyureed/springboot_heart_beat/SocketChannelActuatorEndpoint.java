package io.github.xiaoyureed.springboot_heart_beat;

import io.github.xiaoyureed.springboot_heart_beat.server.SocketChannelHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.Map;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
//@Component
//@Profile("server")
//@Endpoint(id = "channel_map")
public class SocketChannelActuatorEndpoint{

    @ReadOperation
    public Map<Long, NioSocketChannel> map() {
        return SocketChannelHolder.map();
    }
}
