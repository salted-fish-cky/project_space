package com.cky.crm;

import com.cky.crm.beans.Customer;
import com.cky.crm.service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cxf.xml");
        CustomerService service = (CustomerService) context.getBean("service");
        List<Customer> all = service.findAll();
        System.out.println(all.get(0).getAddress());

    }
}
