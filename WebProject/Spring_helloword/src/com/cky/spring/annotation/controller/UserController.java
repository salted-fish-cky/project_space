package com.cky.spring.annotation.controller;

import com.cky.spring.annotation.TestObject;
import com.cky.spring.annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    /**
     * @ Autowired注解自动装配具有兼容类型的单个Bean属性
     */
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private TestObject testObject;
    public void execute(){
        System.out.println("userController execute...");
        userService.add();
        System.out.println(testObject);
    }
}
