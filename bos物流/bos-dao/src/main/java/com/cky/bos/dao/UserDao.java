package com.cky.bos.dao;

import com.cky.bos.domain.User;

public interface UserDao extends BaseDao<User> {
    User findByUserNameAndPassword(String username,String password);

    User findUserByUsername(String username);
}
