package com.cky.bookstore.service;

import com.cky.bookstore.dao.AccountDao;
import com.cky.bookstore.domian.Account;
import com.cky.bookstore.impl.AccountDaoImpl;

public class AccountService {

    private AccountDao accountDao = new AccountDaoImpl();
    public Account getAccount(int accountId){
        return accountDao.getAccount(accountId);
    }
}
