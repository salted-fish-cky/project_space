package com.cky.spring.jdbc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest  {
    private ApplicationContext context = null;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    {
        context = new ClassPathXmlApplicationContext("spring-config.xml");
        jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("namedParameterJdbcTemplate");
    }

    /**
     * 使用具名参数时，可以使用update（）方法进行更新操作
     * 1.SQL语句的参数名和类属性一致
     * 2.使用SqlParamterSource的BeanPropertySqlParamterSource 实现作为参数
     */
    @Test
    public void testNamedParameterJdbcTemplate2(){
        String sql = "insert into oder(name,customer_id) values(:name,:customer_id)";
        Oder oder = new Oder();
        oder.setName("jj");
        oder.setCustomer_id(11);
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(oder);
        namedParameterJdbcTemplate.update(sql,sqlParameterSource);
    }

    /**
     * 可以为参数起名字，
     * 1.好处：若有多个参数，则不用再去对应位置，直接对应参数名，便于维护
     * 2.缺点：操作较为麻烦
     */
    @Test
    public void testNamedParameterJdbcTemplate(){
        String sql = "insert into oder(name,customer_id) values(:name,:c_id)";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("name","xiaoMing");
        paramMap.put("c_id","10");
        namedParameterJdbcTemplate.update(sql,paramMap);

    }

    /**
     * 查找实体类的集合
     */
    @Test
    public  void testQueryForList(){
        String sql = "select id,name,customer_id from oder where id>?";
        RowMapper<Oder> rowMapper = new BeanPropertyRowMapper<>(Oder.class);
        List<Oder> oders = jdbcTemplate.query(sql,rowMapper,9);
        System.out.println(oders);
    }

    /**
     * 从数据库中获取一条记录，实际得到一个对象
     * 1.其中的RowMapper指定如何去映射结果集的行，常用的实现类为BeanPropertyRowMapper
     */
    @Test
    public void testQueryforObject(){
        String sql = "select id,name,customer_id from oder where id=?";
        RowMapper<Oder> rowMapper = new BeanPropertyRowMapper<>(Oder.class);
        Oder oder = jdbcTemplate.queryForObject(sql, rowMapper, 9);
        System.out.println(oder);
    }

    /**
     * 执行insert,update,delete
     */
    @Test
    public void testUpdate(){
        String sql = "update oder set name=? where id=?";
        jdbcTemplate.update(sql,"jack",8);
    }

    /**
     * 执行批量更新
     * 最后一个参数是Object【】 的LIst类型：因为修改一条记录是一个Object的数组，那么多条就
     * 需要多个Object的集合
     */
    @Test
    public void testBatchUpdate(){
        String sql = "insert into oder(name,customer_id) values(?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[] {"tom",9});
        batchArgs.add(new Object[] {"cky",10});
        batchArgs.add(new Object[] {"nv",9});
        batchArgs.add(new Object[] {"sa",11});
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Test
    public void testDataSource(){
        DataSource dataSource = context.getBean(DataSource.class);
        try {
            System.out.println(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
