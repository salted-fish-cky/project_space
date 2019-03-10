package com.cky.bookstore.impl;

import com.cky.bookstore.dao.DAO;
import com.cky.bookstore.dao.UserDao;
import com.cky.bookstore.domian.User;

public class UserDaoImpl extends DAO<User> implements UserDao{

    @Override
    public User getUser(String username) {
        String sql = "select * from userinfo where username=?";
        return get(sql,username);
    }
}
