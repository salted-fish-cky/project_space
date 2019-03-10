package com.cky.maven.ssh.dao;

import com.cky.maven.ssh.entities.User;

public interface UserDao {

    public User findById(Integer id);
}
