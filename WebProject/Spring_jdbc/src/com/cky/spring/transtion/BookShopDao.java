package com.cky.spring.transtion;

import org.springframework.stereotype.Repository;

public interface BookShopDao {
    //根据书好获取书的单价
    public double findBookPriceByIsbn(int isbn);

    //更新书的库存，使书号对应的库存减1
    public void updateBookStock(int isbn);

    //更新用户的账号余额，使username的balance-price
    public void updateUserBalanceAccount(String username,double price);
}
