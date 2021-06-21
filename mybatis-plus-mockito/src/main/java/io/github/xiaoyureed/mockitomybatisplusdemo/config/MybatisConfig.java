package io.github.xiaoyureed.mockitomybatisplusdemo.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/17
 */
@Configuration
public class MybatisConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean result = new SqlSessionFactoryBean();
        result.setDataSource(dataSource);
        result.setPlugins(new SqlInterceptor());
        return result;
    }

    /**
     * 在mybatis中可被拦截的类型有四种(按照拦截顺序):
     * <p>
     * Executor：执行 SQL 语句。
     * ParameterHandler：处理参数
     * ResultHandler：处理结果集。
     * StatementHandler：Sql语法构建。
     *
     * 每个类型可以被拦截的方法:
     *
     拦截的类	拦截的方法
     Executor	update, query, flushStatements, commit, rollback,getTransaction, close, isClosed
     ParameterHandler	getParameterObject, setParameters
     StatementHandler	prepare, parameterize, batch, update, query
     ResultSetHandler	handleResultSets, handleOutputParameters
     */
    //标识该类是一个拦截器；
    @Intercepts({
            //指明自定义拦截器需要拦截哪一个类型，哪一个方法(方法有重载, 所以还需要 指明 args 才能确定是哪个方法)
            @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
            @Signature(type = Executor.class, method = "query", args = {
                    MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
    })
    private static class SqlInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
//            Object target = invocation.getTarget(); //被代理对象
//            Method method = invocation.getMethod(); //代理方法
//            Object[] args = invocation.getArgs(); //方法参数

            Object[] args = invocation.getArgs();
            Object param = null;
            MappedStatement mappedStatement = (MappedStatement) args[0];
            if (args.length > 1) {
                param = args[1];
            }
            String sql = sql(mappedStatement, param);
            return invocation.proceed();
        }

        private String sql(MappedStatement statement, Object param) {
            BoundSql boundSql = statement.getBoundSql(param);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            //将原始sql中的空白字符（\s包括换行符，制表符，空格符）替换为" "
            String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
            if (param == null || parameterMappings.size() == 0) {
                return sql;
            }

            org.apache.ibatis.session.Configuration configuration = statement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(param.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(param));
            } else {
                MetaObject metaObject = configuration.newMetaObject(param);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
            return sql;
        }

        private String getParameterValue(Object obj) {
            String value = null;
            if (obj instanceof String) {
                value = "'" + obj.toString() + "'";
            } else if (obj instanceof Date) {
                DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
                value = "'" + formatter.format(obj) + "'";
            } else {
                if (obj != null) {
                    value = obj.toString();
                } else {
                    value = "";
                }
            }
            return value;
        }

        /**
         * 可选
         * 我们可以决定是否要进行拦截, 如果拦截, 就返回代理后的对象
         *       * 如果不拦截, 则返回原始对象
         *
         *  每经过一个拦截器对象都会调用插件的plugin方法，也就是说，该方法会调用4次。根据@Intercepts注解来决定是否进行拦截处理
         */
        @Override
        public Object plugin(Object target) {
            //返回四大接口对象的代理对象, 表示始终会执行拦截
            return Plugin.wrap(target, this);

            // 控制是否拦截
//            if (target instanceof StatementHandler) {
//                return Plugin.wrap(target, this);
//            }
//            return target;
        }

        /**
         * 可选
         * 用于在 Mybatis 配置文件中指定一些属性的。类似 spring 中有 @value,
         * 一般不用
         */
        @Override
        public void setProperties(Properties properties) {
        }
    }
}
