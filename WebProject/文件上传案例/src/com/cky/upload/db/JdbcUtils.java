package com.cky.upload.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * jdbc操作的工具类
 */
public class JdbcUtils {
    /**
     * 释放Connection 连接
     * @param connection
     */
    public static void releaseConnection(Connection connection){
        try{
            if(connection!= null){
                connection.close();
            }

        }catch (Exception e){

        }
    }

    private static ComboPooledDataSource dataSource = null;

    /**
     * 静态代码块，只会被执行一次
     */
    static {
        try {
            dataSource = new ComboPooledDataSource("mvcapp");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 返回数据源的Connection 对象
     * @return
     */
    public static Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }
}
