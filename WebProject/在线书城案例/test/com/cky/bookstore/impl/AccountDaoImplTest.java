package com.cky.bookstore.impl;

import com.cky.bookstore.dao.AccountDao;
import com.cky.bookstore.dao.UserDao;
import com.cky.bookstore.domian.Account;
import com.cky.bookstore.domian.User;


public class AccountDaoImplTest {
    AccountDao accountDao = new AccountDaoImpl();
    UserDao userDao = new UserDaoImpl();
    @org.junit.Test
    public void get(){
        Account account = accountDao.getAccount(1);
        System.out.println(account.getBalance());

    }

    @org.junit.Test
    public void updateBalance() throws Exception {
        accountDao.updateBalance(1,100);
    }

    @org.junit.Test
    public void getUser() throws Exception {
        User cky = userDao.getUser("cky");
        System.out.println(cky);
    }
}
