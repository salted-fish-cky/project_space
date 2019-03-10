package com.cky.hibernate.test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import org.junit.Test;

import java.util.Date;


public class HibernateTest {
    @Test
    public void test(){
        //1.创建一个SessionFactory
        SessionFactory sessionFactory = null;
        try{
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
            configuration.addClass(News.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        }catch (Exception e){
            e.printStackTrace();
        }
        //2.创建一个Session
        System.out.println(sessionFactory);
        Session session =  sessionFactory.openSession();
        //3.开启事物
        Transaction transaction =  session.beginTransaction();
        //4.执行保存操作
        News news = new News("zz", "cky", new Date());
        session.save(news);
        //5.提交事物
        transaction.commit();
        //6.关闭Session
        session.close();
        //7.关闭SessionFactory
        sessionFactory.close();
    }
}
