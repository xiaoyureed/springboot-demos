package io.github.xiaoyureed.springboot_heart_beat.client;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
@Configuration
@Profile({"client0", "client1"})
public class ClientConfig {

    @Profile("client0")
    @Bean
    public HeartbeatProto ping0() {
        return new HeartbeatProto(0L, "ping");
    }

    @Profile("client1")
    @Bean
    public HeartbeatProto ping1() {
        return new HeartbeatProto(1L, "ping");
    }
}
