package com.cky.hibernate.test;

import com.cky.hibernate.bean.Customer;
import com.cky.hibernate.bean.Oder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
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
//        configure.addClass(Customer.class);
//        configure.addClass(Oder.class);
//        configure.addResource("com/cky/hibernate/bean/Customer.hbm.xml");
//        configure.addResource("com/cky/hibernate/bean/Oder.hbm.xml");
//        org.hibernate.service.ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configure.getProperties()).build();
//        sessionFactory = configure.buildSessionFactory(serviceRegistry);
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

    }


    @After
    public void destory(){
        System.out.println("destory");
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testUpdate(){
        Oder oder = session.get(Oder.class, 8);
        oder.getCustomer().setName("cky");
    }

    @Test
    public void testDelete(){
        //在不设定级联关系的情况下
        Customer customer = session.get(Customer.class, 9);
        session.delete(customer);
    }

    @Test
    public void testManyOneGet(){
        //若查询多的一端的一个对象，默认情况下，致查询了多的一端的
        Oder oder = session.get(Oder.class, 9);
        System.out.println(oder);
        System.out.println(oder.getName());

        Customer customer = oder.getCustomer();
        System.out.println(customer.getName());

    }

    @Test
    public void testManyOneSave(){
        Customer customer = new Customer();
        customer.setName("BB");

        Oder oder1 = new Oder();
        oder1.setName("oder3");
        Oder oder2 =new Oder();
        oder2.setName("oder4");
        //设定关联关系
        oder1.setCustomer(customer);
        oder2.setCustomer(customer);

        //执行save
        session.save(customer);
        session.save(oder1);
        session.save(oder2);
    }

    /**
     * fetch属性检索：
     * set集合的fetch属性：
     * 1.默认值select，通过正常的方式来初始化set元素
     * 2.可以取值为subselect，通过子查询的方式来初始化所有的set集合，子查询
     * 作为where子句的in的条件出现，子查询查询所有1的一端的id，此时lazy有效，但batch-size失效
     *
     * 3.若取值为join，则在加载1的一端的对像时，使用迫切左外链接的方式检索n的一端的集合属性
     */
    @Test
    public void testSetFetch(){
        List<Customer> customers = session.createQuery("FROM Customer").list();
        System.out.println(customers.size());

        for(Customer customer:customers){
            if(customer.getOderSet()!=null){
                System.out.println(customer.getOderSet().size());
            }
        }
    }
    @Test
    public void testSetFetch2(){
        Customer customer = session.get(Customer.class, 9);
        System.out.println(customer.getOderSet().size());
    }

    /**
     * batch-size 检索
     * set 元素的batch-size 属性：设定一次初始化set集合的数量，能减少检索的select语句
     */
    @Test
    public void testSetBatchSize(){
        List<Customer> customers = session.createQuery("FROM Customer").list();
        System.out.println(customers.size());

        for(Customer customer:customers){
            if(customer.getOderSet()!=null){
                System.out.println(customer.getOderSet().size());
            }
        }


    }


    /**
     * lazy检索
     * //-----------set 的 lazy 属性-----------
     //1.1-n或n-n的集合属性默认使用懒加载检索策略
     //2.可以通过设置setlazy属性来修改默认的检索策略，默认为true
     //3.lazy还可以设置为extra，增强的延迟检索
     */
    @Test
    public void testSetLazy(){
        Customer customer = session.get(Customer.class, 9);
        System.out.println(customer.getName());
        System.out.println(customer.getOderSet().size());


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
