package io.github.xiaoyureed.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
public class HandlerRegistry {
    private final Map<String, IMsgHandler<?>> handlers = new HashMap<>();
    private IMsgHandler<MsgIn> defaultHandler;

    public void register(String type, IMsgHandler<?> handler) {
        handlers.put(type, handler);
    }

    public HandlerRegistry defaultHandler(IMsgHandler<MsgIn> defaultHandler) {
        this.defaultHandler = defaultHandler;
        return this;
    }

    public IMsgHandler<MsgIn> defaultHandler() {
        return defaultHandler;
    }

    public IMsgHandler<?> get(String type) {
        return handlers.get(type);
    }
}
