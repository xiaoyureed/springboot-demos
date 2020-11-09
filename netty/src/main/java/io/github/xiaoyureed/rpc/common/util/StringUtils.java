package io.github.xiaoyureed.rpc.common.util;

import java.util.UUID;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
public final class StringUtils {
    private StringUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
