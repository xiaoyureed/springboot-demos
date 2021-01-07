package io.github.xiaoyureed.springbootdemos.rwseparatedemo;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/16
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatasourceRouteAspect {

}
