package com.cky.bookstore.dao;

import com.cky.bookstore.domian.User;

public interface UserDao {

    /**
     * 根据用户名获取用户的User
     * @param username
     * @return
     */
    public abstract User getUser(String username);
}
