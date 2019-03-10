package com.cky.maven.ssh.service;

import com.cky.maven.ssh.entities.User;

public interface UserService {

    public User findById(Integer id);
}
