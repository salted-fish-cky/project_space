package com.cky.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cxf.xml");
        CustomerService myClient = (CustomerService) context.getBean("myClient");
        List<Customer> all = myClient.findAll();
        System.out.println(all.get(0));


    }
}
