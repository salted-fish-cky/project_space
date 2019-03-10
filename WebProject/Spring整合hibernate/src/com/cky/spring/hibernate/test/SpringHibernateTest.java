package com.cky.spring.hibernate.test;

import com.cky.spring.hibernate.service.BookShopService;
import com.cky.spring.hibernate.service.Cashier;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

public class SpringHibernateTest {

    private ApplicationContext context;
    private BookShopService bookShopService;
    private Cashier cashier;
    {
        context = new ClassPathXmlApplicationContext("spring-config.xml");
        bookShopService = context.getBean(BookShopService.class);
        cashier = context.getBean(Cashier.class);
    }

    @Test
    public void testCasher(){
        cashier.checkout("cky", Arrays.asList(1001,1002));
    }

    @Test
    public void test() throws SQLException {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testBookShopService(){
        bookShopService.purchase("cky",1001);
    }
}
