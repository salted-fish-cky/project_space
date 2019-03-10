package com.cky.bookstore.dao;

import com.cky.bookstore.domian.Account;

public interface AccountDao {
    /**
     * 获取account
     * @param accountId
     * @return
     */
    public abstract Account getAccount(Integer accountId);

    public abstract void updateBalance(Integer accountId,float amount);
}
