package com.cky.bookstore.dao;

import com.cky.bookstore.db.JdbcUtils;
import com.cky.bookstore.web.ConnectionContext;
import jdk.nashorn.internal.scripts.JD;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 *封装了 CRUD的方法，以供类继承使用
 * 当前dao直接获取数据库连接
 * @param <T> 当前的dao处理的数据类型
 */
public class DAO<T> {
    private QueryRunner queryRunner = new QueryRunner();
    private Class<T> clazz;

    public DAO(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if(actualTypeArguments!=null&&actualTypeArguments.length>0){
                if(actualTypeArguments[0] instanceof Class){
                    clazz = (Class<T>) actualTypeArguments[0];
                }
            }
        }
    }

    /**
     * 返回某条数据中的某个值
     * @param sql
     * @param args
     * @param <E>
     * @return
     */
    public <E> E getForValue(String sql,Object...args){
        Connection connection = null;
        try{
            connection = ConnectionContext.getInstance().get();
            return (E)queryRunner.query(connection,sql,new ScalarHandler(),args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 返回T对应的List
     * @param sql
     * @param args
     * @return
     */
    public List<T> getForList(String sql,Object...args){
        Connection connection = null;
        try{
            connection = ConnectionContext.getInstance().get();
            return queryRunner.query(connection,sql,new BeanListHandler<>(clazz),args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 返回T对应实体类对象
     * @param sql
     * @return
     */
    public T get(String sql,Object...args){

        Connection connection = null;
        try{
            connection = ConnectionContext.getInstance().get();
            return queryRunner.query(connection,sql,new BeanHandler<>(clazz),args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 该方法封装了insert，delete，update操作，
     * sql是sql语句
     * @param sql
     * @param args
     */

    public void update(String sql,Object...args){
        Connection connection = null;
        try {
           connection =  ConnectionContext.getInstance().get();
           queryRunner.update(connection,sql,args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void batch(String sql,Object[]...args){
        Connection connection = null;
        try {
            connection = ConnectionContext.getInstance().get();
            queryRunner.batch(connection,sql,args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public long insert(String sql,Object...args){
        long id = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = ConnectionContext.getInstance().get();
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(args!=null){
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1,args[i]);
                }

            }

            preparedStatement.executeUpdate();
            //获取生成的主键值
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                id = resultSet.getLong(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.release(resultSet,preparedStatement);
        }
        return id;
    }


}
