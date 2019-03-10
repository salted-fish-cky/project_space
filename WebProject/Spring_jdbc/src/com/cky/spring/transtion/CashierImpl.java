package com.cky.spring.transtion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("cashier")
public class CashierImpl implements Cashier{
    @Autowired
    private BookShopService bookShopService;
    @Transactional //添加事务注解
    @Override
    public void checkout(String username, List<Integer> isbns) {
        for(int isbn:isbns){
            bookShopService.purchase(username,isbn);
        }
    }
}
