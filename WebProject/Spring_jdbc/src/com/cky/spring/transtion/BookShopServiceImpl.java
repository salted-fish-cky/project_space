package com.cky.spring.transtion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("bookShopService")
public class BookShopServiceImpl implements BookShopService{

    @Autowired
    private BookShopDao bookShopDao;
    //添加事务注解
    //使用propagation指定事务的传播行为，即当前事务的方法被另一个事务方法调用时，
    //如何使用事务，默认取值REQUIRED，即使用调用方法的事务
    //默认取值REQUIRED_NEWS:使用自己的事务，调用事务方法的事务被挂起
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void purchase(String username, int isbn) {
        //1.获取书的单价
        Double price = bookShopDao.findBookPriceByIsbn(isbn);
        //2.更新书的库存
        bookShopDao.updateBookStock(isbn);
        //3.更新用户余额
        bookShopDao.updateUserBalanceAccount(username,price);

    }
}
