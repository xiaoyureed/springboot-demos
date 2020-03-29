package io.github.xiaoyureed.springbootdemos.common.util;

import java.util.Collection;

/**
 * @author xiaoyu
 * date: 2020/3/24
 */
public final class CollectionUtils {
    public static boolean isEmpty(Collection collection) {
        return (collection == null) || collection.size() == 0;
    }
}
