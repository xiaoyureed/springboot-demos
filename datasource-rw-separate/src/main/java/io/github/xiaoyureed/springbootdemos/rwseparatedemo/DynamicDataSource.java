package io.github.xiaoyureed.springbootdemos.rwseparatedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 承自 AbstractRoutingDataSource 类，在访问数据库时会调用该类
 * 的 determineCurrentLookupKey() 方法获取数据库实例的 key
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/16
 */
@Component
@Primary // 存在多个DataSource , 以这个数据源为准
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DatasourceRouteHolder routeHolder;

    @Autowired
    @Qualifier("master")
    private DataSource master;

    @Autowired
    @Qualifier("slave1")
    private DataSource slave1;


    /**
     * 返回生效的数据源名称
     */
    @Override
    protected Object determineCurrentLookupKey() {
        final String route = routeHolder.getRoute();
        log.debug(">>> choose datasource route: {}", route);
        return route;
    }


    /**
     * 配置使用的数据源信息，如果不存在就使用默认的数据源
     */
    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("master", master);
        dsMap.put("slave1", slave1);

        super.setTargetDataSources(dsMap);
        super.setDefaultTargetDataSource(master);

        super.afterPropertiesSet();
    }

/////////////////// 下面的部分可用于改造成为 saas 系统动态添加数据源 /////////////////////////

//    /**
//     * 自己维护一个 targetDataSource
//     */
//    private Map<Object, Object> dataSources = new ConcurrentHashMap<>();
//
//
//    @Override
//    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
//        super.setTargetDataSources(targetDataSources);
//        this.dataSources = targetDataSources;
//    }
//
//    /**
//     * add DataSource
//     *
//     * @param route tenant id
//     */
//    public DynamicDataSource addDataSource(String route, DataSource datasource) {
//        this.dataSources.put(route, datasource);
//        super.setTargetDataSources(this.dataSources);
//        super.afterPropertiesSet(); // 动态添加数据源后必须调用, 才会生效
//        return this;
//    }
}
