package io.github.xiaoyureed.rpc.common;

import io.github.xiaoyureed.rpc.common.util.JsonUtils;
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
public class MsgIn {
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息唯一ID, 用于客户端验证服务器请求和响应是否匹配
     */
    private String requestId;
    /**
     * json序列化字符串
     */
    private String payload;

    public <T> T parsePayload(Class<T> clazz) {
        return JsonUtils.toObj(payload, clazz);
    }
}
