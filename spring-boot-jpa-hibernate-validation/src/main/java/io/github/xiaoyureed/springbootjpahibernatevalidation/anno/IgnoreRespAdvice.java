package io.github.xiaoyureed.springbootjpahibernatevalidation.anno;

import java.lang.annotation.*;

/**
 * 用来忽略 rest 返回结果的处理
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/4
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreRespAdvice {
}
