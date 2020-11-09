package io.github.xiaoyureed.rpc.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/9
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
    }

    private JsonUtils() {
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(">>> error of parse json string", e);
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(">>> error of encoding json", e);
            throw new RuntimeException(e);
        }
    }
}
