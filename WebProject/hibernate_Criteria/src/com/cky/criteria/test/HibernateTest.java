package com.cky.criteria.test;


import com.cky.criteria.entity.Customer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class HibernateTest {

    private SessionFactory sessionFactory;

    private Session session;

    private Transaction transaction;

    @Before
    public void init(){
        System.out.println("init");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }


    /**
     * 条件查询：
     *
     * hql语句，不可能出现数据库的相关信息
     * Criteria 中对应的方法
     * >                gt
     * >=               ge
     * <                lt
     * <=               le
     * ==               eq
     * !=               ne
     * in               in
     * between and      between
     * like             like
     * is null          isNull
     * is not null      isNotNull
     * or               or
     * and              and
     */

    @Test
    public void testCriteria(){
        //创建Criteria查询对象
        Criteria criteria = session.createCriteria(Customer.class);
        //添加查询参数=>查询id为1的Customer对象
//        criteria.add(Restrictions.eq("custId",1));

        //执行分页查询
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);

        //执行查询获得结果
        Customer customer = (Customer) criteria.uniqueResult();
        System.out.println(customer);
    }

    /**
     * 离线查询:现在web层或service层拼装条件，然后再传到dao层查询
     */
    @Test
    public void testDetachedCriteria(){
        //Service层/web层
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Customer.class);
        //拼装条件
        detachedCriteria.add(Restrictions.idEq(1));

        //----------------------------------------

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        List list = criteria.list();
        System.out.println(list);

    }

    @After
    public void destory(){
        System.out.println("destory");
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testDowork(){
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println(connection);
                //调用存储过程
            }
        });
    }

}
