package com.cky.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println("基于被CXF开发的sayHello方法被调用了。。。");

        return "hello"+name;
    }
}
