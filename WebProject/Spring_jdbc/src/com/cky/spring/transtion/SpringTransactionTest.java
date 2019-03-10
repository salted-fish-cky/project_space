package com.cky.spring.transtion;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class SpringTransactionTest {
    private ApplicationContext context;
    private BookShopDao bookShopDao = null;
    private BookShopService bookShopService;
    private Cashier cashier;

    {
        context = new ClassPathXmlApplicationContext("spring-config.xml");
        bookShopDao = context.getBean(BookShopImpl.class);
        bookShopService = context.getBean(BookShopService.class);
        cashier = context.getBean(Cashier.class);
    }

    @Test
    public void testBookShopDaoUpdateUserAccount(){
        bookShopDao.updateUserBalanceAccount("cky",200);
    }

    @Test
    public void testBookShopDaoFindPriceByIsbn(){
        System.out.println(bookShopDao.findBookPriceByIsbn(1001));
    }

    @Test
    public void testBookShopDaoUpdateBookStock(){
        bookShopDao.updateBookStock(1001);
    }


    @Test
    public void testBookShopService(){
        bookShopService.purchase("cky",1001);
    }

    @Test
    public void testTransationPropagation(){
        cashier.checkout("cky", Arrays.asList(1001,1002));
    }
}
