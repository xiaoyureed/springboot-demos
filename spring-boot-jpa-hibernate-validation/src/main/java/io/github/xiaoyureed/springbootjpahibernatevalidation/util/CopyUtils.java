package io.github.xiaoyureed.springbootjpahibernatevalidation.util;

import org.springframework.cglib.beans.BeanCopier;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
public final class CopyUtils {

    private CopyUtils(){}

    public static <S, T> T copyBean(S source, T target) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
        return target;
    }

}
