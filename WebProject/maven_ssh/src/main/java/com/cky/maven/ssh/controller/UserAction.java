package com.cky.maven.ssh.controller;

import com.cky.maven.ssh.entities.User;
import com.cky.maven.ssh.service.UserService;
import com.opensymphony.xwork2.ActionSupport;



public class UserAction extends ActionSupport{

    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User user;

    public void setUser(User user) {
        this.user = user;
    }

    public String findById(){
        user = userService.findById(id);
        System.out.println(user.getTel());
        return SUCCESS;
    }


}
