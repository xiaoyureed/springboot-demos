package io.github.xiaoyureed.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
public class MsgRegistry {
    private final Map<String, Class<?>> classes = new HashMap<>();

    public void register(String type, Class<?> clazz) {
        classes.put(type, clazz);
    }

    public Class<?> get(String type) {
        return classes.get(type);
    }
}
