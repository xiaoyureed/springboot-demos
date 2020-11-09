package io.github.xiaoyureed.rpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgOut {
    private String requestId;
    private String type;
    private Object payload;


}
