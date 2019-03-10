package com.cky.maven.ssh.service.impl;

import com.cky.maven.ssh.dao.UserDao;
import com.cky.maven.ssh.entities.User;
import com.cky.maven.ssh.service.UserService;

public class UserServiceImpl implements UserService  {

    public UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findById(Integer id) {

        return userDao.findById(id);
    }
}
