package com.cky.spring.hibernate.dao;

import com.cky.spring.hibernate.exception.BookStockException;
import com.cky.spring.hibernate.exception.UserAccountException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookShopDaoImpl implements BookShopDao {

    @Autowired
    private SessionFactory sessionFactory;


    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public double findBookPriceByIsbn(int isbn) {
        String hql = "select b.price from Book b where b.isbn=?";
        Query query = getSession().createQuery(hql).setParameter(0, isbn);
        return (double) query.uniqueResult();
    }

    @Override
    public void updateBookStock(int isbn) {
        //验证书的库存是否充足
        String hql2 = "select b.stock from BookStock b where b.isbn=?";
        int stock = (int) getSession().createQuery(hql2).setParameter(0, isbn).uniqueResult();
        if(stock == 0){
            throw new BookStockException("库存不足！");
        }

        String hql = "update BookStock b set b.stock=b.stock-1 where b.isbn=?";
        getSession().createQuery(hql).setParameter(0, isbn).executeUpdate();
    }

    @Override
    public void updateUserBalanceAccount(String username, double price) {
        //验证余额是否充足
        String sql2 = "select a.balance from Account a where a.username=?";
        double balance = (double) getSession().createQuery(sql2).setParameter(0,username).uniqueResult();
        if (balance<price){
            throw new UserAccountException("余额不足！");
        }
        String hql = "update Account a set a.balance=a.balance-? where a.username=?";
        getSession().createQuery(hql).setParameter(0,price).setParameter(1,username).executeUpdate();
    }
}
