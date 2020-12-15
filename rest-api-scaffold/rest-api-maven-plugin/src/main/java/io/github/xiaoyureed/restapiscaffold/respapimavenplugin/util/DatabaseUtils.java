package io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.GlobalConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyu
 * @date 2019/5/14
 */
public final class DatabaseUtils {

    private DatabaseUtils() {
    }

    /**
     * @return a map, key - column name, value - full class name
     */
    public static Map<String, String> getColumns(GlobalConfig config) {
        final HashMap<String, String> result            = new HashMap<>(10);
        final Connection              conn              = getConn(config);
        PreparedStatement             preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(" select * from " + config.tableName + " limit 0");
            final ResultSetMetaData metaData = preparedStatement.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final String columnName      = metaData.getColumnName(i);
                final String columnClassName = metaData.getColumnClassName(i);// full name
                result.put(columnName, columnClassName);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(">>> sql error", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(">>> Error of closing resources", e);
                }
            }
        }
    }

    private static Connection getConn(GlobalConfig config) {
        try {
            Class.forName(config.driverClassName);
            return DriverManager.getConnection(config.jdbcUrl, config.username, config.password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(">>> Error of getConn", e);
        }
    }


}