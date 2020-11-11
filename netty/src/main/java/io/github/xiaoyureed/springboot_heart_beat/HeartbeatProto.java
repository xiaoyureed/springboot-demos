package io.github.xiaoyureed.springboot_heart_beat;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/11
 */
@Data
@AllArgsConstructor
public class HeartbeatProto {
    /**
     * header, 8 bytes
     */
    private long id;
    private String content;
}
