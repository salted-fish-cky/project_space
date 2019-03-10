package com.cky.spring.hibernate.service;

import com.cky.spring.hibernate.dao.BookShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookShopServiceImpl implements BookShopService {

    @Autowired
    private BookShopDao bookShopDao;
    @Override
    public void purchase(String username, int isbn) {
        double price = bookShopDao.findBookPriceByIsbn(isbn);
        bookShopDao.updateBookStock(isbn);
        bookShopDao.updateUserBalanceAccount(username,price);
    }
}
