package io.github.xiaoyureed.concurrentjava.util;

/**
 * @author xiaoyu
 * @since 1.0
 */
public final class ObjectUtils {

    private ObjectUtils() {}

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> clazz) {
        return (T) obj;
    }
}
