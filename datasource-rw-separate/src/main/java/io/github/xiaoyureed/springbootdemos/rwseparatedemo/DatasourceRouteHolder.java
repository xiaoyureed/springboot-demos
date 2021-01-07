package io.github.xiaoyureed.springbootdemos.rwseparatedemo;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/16
 */
@Component
public class DatasourceRouteHolder {
    private static final ThreadLocal<String> route = new ThreadLocal<>();

    private static final Set<Object> routeKeys = new HashSet<>();

    public void setRoute(String datasource) {
        route.set(datasource);
    }

    public String getRoute() {
        return route.get();
    }

    public void clear() {
        route.remove();
    }

    public Boolean checkRouteKey(String routeKey) {
        return routeKeys.contains(routeKey);
    }


    public void addRouteKey(String... routeKey) {
        routeKeys.addAll(Arrays.asList(routeKey));
    }
}
