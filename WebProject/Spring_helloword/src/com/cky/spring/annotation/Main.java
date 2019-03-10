package com.cky.spring.annotation;

import com.cky.spring.annotation.controller.UserController;
import com.cky.spring.annotation.repository.UserRepository;
import com.cky.spring.annotation.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-annotation.xml");
//        TestObject to = (TestObject) context.getBean("testObject");
//        System.out.println(to);
//        UserController userController = (UserController) context.getBean("userController");
//        System.out.println(userController);
//        UserRepository userRepository = (UserRepository) context.getBean("userRepository");
//        System.out.println(userRepository);
//        UserService userService = (UserService) context.getBean("userService");
//        System.out.println(userService);
        UserController userController = (UserController) context.getBean("userController");
        userController.execute();
    }
}
