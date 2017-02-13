package util;

import config.Config;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 * Created by cwen on 17-2-13.
 */
public class JDBCUtil{
    private static DataSource ds;

    public static Connection getConnection() {
        Connection connection = null;
        try {
            DataSource ds = getDataSource(Config.driverName,Config.url,Config.userName,
                    Config.password, Config.initialSize, Config.maxSize, Config.maxIdle);
            connection = ds.getConnection();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DataSource getDataSource(String driverName, String url,
                                           String userName, String password, int initialSize, int maxSize,
                                           int maxIdle) {
        if (ds == null) {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName(driverName);
            bds.setUrl(url);
            bds.setUsername(userName);
            bds.setPassword(password);
            bds.setInitialSize(initialSize);
            bds.setMaxTotal(maxSize);
            bds.setMaxIdle(maxIdle);
            ds = bds;
        }
        return ds;
    }

    public static void closeConnection(ResultSet set, Statement statement, Connection conn) {
        try{
            if(set != null) {
                set.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
