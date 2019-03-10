package com.cky.spring.hibernate.service;

import com.cky.spring.hibernate.dao.BookShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierImpl implements Cashier {

    @Autowired
    private BookShopService bookShopService;
    @Override
    public void checkout(String username, List<Integer> isbns) {
        for (int isbn:isbns){
            bookShopService.purchase(username,isbn);
        }
    }
}
