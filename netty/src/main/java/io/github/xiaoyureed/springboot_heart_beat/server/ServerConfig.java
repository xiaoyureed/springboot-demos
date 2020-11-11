package io.github.xiaoyureed.springboot_heart_beat.server;

import io.github.xiaoyureed.springboot_heart_beat.HeartbeatProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */

@Configuration
@Profile("server")
public class ServerConfig {
    @Bean
    public HeartbeatProto pong() {
        return new HeartbeatProto(0x888888, "pong");
    }
}
