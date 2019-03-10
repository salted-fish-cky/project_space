package com.cky.bookstore.impl;

import com.cky.bookstore.dao.AccountDao;
import com.cky.bookstore.dao.DAO;
import com.cky.bookstore.domian.Account;

public class AccountDaoImpl extends DAO<Account> implements AccountDao {
    @Override
    public Account getAccount(Integer accountId) {
        String sql = "select * from account where accountid=?";
        return get(sql,accountId);
    }

    @Override
    public void updateBalance(Integer accountId, float amount) {
        String sql = "update account set balance=balance-? where accountid=?";
        update(sql,amount,accountId);
    }
}
