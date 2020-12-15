package io.github.xiaoyureed.restapiscaffold.respapimavenplugin;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util.DatabaseUtils;

import java.util.Map;

/**
 * @author : xiaoyureed
 * 2020/7/28
 */
public class DatabaseUtilsTest {
    public static void main(String[] args) {
        testGetColumns();
    }

    private static void testGetColumns() {
        GlobalConfig config = GlobalConfig.me();
        config.tableName = "account";
        config.jdbcUrl = "jdbc:mysql://192.168.1.107:3307/micro";
        config.driverClassName = "com.mysql.cj.jdbc.Driver";
        config.username = "root";
        config.password = "root";

        Map<String, String> columns = DatabaseUtils.getColumns(config);
        System.out.println(columns);
    }
}
