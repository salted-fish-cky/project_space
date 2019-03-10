package com.cky.sparkproject.jdbc;

import com.cky.sparkproject.conf.ConfigurationManager;
import com.cky.sparkproject.constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class JdbcHelper {

    private static JdbcHelper jdbcHelper;

    static {
        try {
            String driver = ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 数据库连接池
    private LinkedList<Connection> datasource = new LinkedList<Connection>();

    private JdbcHelper(){
        int datasourceSize = ConfigurationManager.getInteger(Constants.JDBC_DATASOURCE_SIZE);

        Boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        // 然后创建指定数量的数据库连接，并放入数据库连接池中
        for(int i = 0; i < datasourceSize; i++) {
            String url = null;
            String user = null;
            String password = null;
            if(local){
                url = ConfigurationManager.getProperty(Constants.JDBC_URL);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);

            }else {
                url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
            }
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                datasource.push(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static JdbcHelper getInstance(){

        if(jdbcHelper == null){
            synchronized (JdbcHelper.class){
                if(jdbcHelper == null){
                    jdbcHelper = new JdbcHelper();
                    return jdbcHelper;
                }
            }
        }
        return jdbcHelper;
    }

    /**
     * 第四步，提供获取数据库连接的方法
     * 有可能，你去获取的时候，这个时候，连接都被用光了，你暂时获取不到数据库连接
     * 所以我们要自己编码实现一个简单的等待机制，去等待获取到数据库连接
     *
     */
    public synchronized Connection getConnection() {
        while(datasource.size() == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return datasource.poll();
    }

    /**
     * 执行增删改SQL语句
     * @param sql
     * @param params
     * @return 影响的行数
     */
    public int  executeUpdate(String sql,Object[] params){
        int rtn = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if(params != null && params.length >0){
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i+1,params[i]);
                }

            }
            rtn = ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                datasource.push(conn);
            }
        }
        return rtn;
    }

    /**
     * 执行查询SQL语句
     * @param sql
     * @param params
     * @param callback
     */
    public void executeQuery(String sql,Object[] params,QueryCallback callback){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            if(conn == null){
                System.out.println(1111111);
            }
            ps = conn.prepareStatement(sql);
            if(params != null && params.length>0){
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i+1,params[i]);
                }

            }
            rs = ps.executeQuery();
            callback.process(rs);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                datasource.push(conn);
            }
        }
    }

    public int[] executeBatch(String sql, List<Object[]> paramList){
        int rtn[] = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getConnection();
            // 第一步：使用Connection对象，取消自动提交
            if(conn == null){
                System.out.println(1111111);
            }
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            // 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
            if(paramList != null && paramList.size() > 0){
                for (Object[] objects:paramList){
                    if(objects!=null && objects.length > 0){
                        for (int i = 0; i <objects.length ; i++) {
                            ps.setObject(i+1,objects[i]);
                        }
                        ps.addBatch();
                    }
                }

            }
            // 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
            rtn = ps.executeBatch();
            // 最后一步：使用Connection对象，提交批量的SQL语句
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                datasource.push(conn);
            }
        }
        return rtn;

    }

    /**
     * 内部类
     * 查询回调接口
     */
    public static  interface  QueryCallback{
        /**
         * 处理查询结果
         * @param rs
         * @throws Exception
         */
        void process(ResultSet rs) throws Exception;
    }

}
