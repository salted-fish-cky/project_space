package com.cky.hibernate.test;

import com.cky.hibernate.bean.News;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.spi.ServiceRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class HibernateTest {

    private SessionFactory sessionFactory;

    private Session session;

    private Transaction transaction;

    @Before
    public void init(){
        System.out.println("init");
        Configuration configure = new Configuration().configure();
        configure.addClass(News.class);
        org.hibernate.service.ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configure.getProperties()).build();
        sessionFactory = configure.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

    }

    /**
     * flush：使数据表中的记录和session缓存中的对象的状态保持一致，为了保持一致，则可能会发送对应的
     * 1.在Transsaction的commit（）方法中：先调用session的flush方法，再提交事物
     * 2.flush（）方法会可能会发送sql语句，但不会提交事物
     */
    @Test
    public void testSessionFlush(){
        News news = session.get(News.class, 2);
        news.setAuthor("agfgd");  //可以直接更新数据库里面的信息，根据session的flush方法
    }

    /**
     * refresh():会强制发送select语句，以使Session缓存中的对象状态和数据表中对应的数据一致
     */
    @Test
    public void testRefresh(){

    }

    /**
     * clear():清理缓存
     */
    @Test
    public void TestClear(){
        News news1 = session.get(News.class, 2);
        session.clear();
        News news2 = session.get(News.class,2);
    }

    /**
     * save()方法：
     * 1.使一个临时对象变成持久化对象
     * 2.为对象分配ID
     * 3.在flush缓存时，会发送一条insert语句
     * 4.在save方法之前的id是无效的
     */
    @Test
    public void testSave(){
        News news = new News();
        news.setTitle("zz");
        news.setAuthor("vv");
        news.setDate(new Date());
        session.save(news);
    }

    /**
     * 也是保存一条数据
     * 在调用persist方法之前
     */
    @Test
    public void testPersist(){
        News news = new News();
        news.setTitle("dd");
        news.setAuthor("cc");
        news.setDate(new Date());
        news.setId(3000);
        session.persist(news);
    }

    /**
     * get和load的区别：
     * 1.执行get方法：会立即加载对象，
     * 而执行load方法，若不使用对象，则不会立即执行查询操作，而返回一个代理对象
     *
     * get是立即检索，load是延迟检索
     *
     * 2.若数据库中没有对应的记录，get返回null，load抛出异常
     *
     * 3.load方法可能会抛出LazyInitializationException异常
     */
    @Test
    public void testLoad(){
        News news = session.get(News.class, 1);
        System.out.println(news.getClass().getName());
        System.out.println(news);
    }
    @Test
    public void testGet(){
        News news = session.get(News.class, 1);
        System.out.println(news);
    }

    /**
     * update:
     *1.若更新一个持久化对象，不需要显示的调用update方法，因为在调用Transaction的commit（）方法时，
     * 会先执行session的flush方法
     *
     * 2.更新一个游离对象，需要显示的调用session的update方法，可以把一个游离对象转化成持久化对象
     *
     */
    @Test
    public void testUpdate(){
        News news = session.get(News.class, 1);
        news.setAuthor("Java");
    }

    /**
     * 若id不为null，但数据表中还没有和其对应的记录，会抛出一个异常
     */
    public void testSaveOrUpdate(){
        News news = new News("FFF", "fff", new Date());
        session.saveOrUpdate(news);
    }

    /**
     * delete：执行删除操作，
     */
    @Test
    public void testDelete(){
        News news = new News();
        news.setId(2);
//        News news = session.get(News.class, 2);
        session.delete(news);
    }

    /**
     * evict():从session缓存中把指定的持久化对象移除
     */
    @Test
    public void testEvict(){
        News news1 = session.get(News.class, 1);
        News news2 = session.get(News.class,2);

        news1.setTitle("AA");
        news2.setTitle("BB");

        session.evict(news1);
    }

    @Test
    public void test(){
        System.out.println("test");
        News news = session.get(News.class, 1);
        System.out.println(news);
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
