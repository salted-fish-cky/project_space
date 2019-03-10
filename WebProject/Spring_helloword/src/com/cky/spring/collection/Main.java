package com.cky.spring.collection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Person person = (Person) context.getBean("person4");
        System.out.println(person);

        DataSource dataSource = (DataSource) context.getBean("dataSource");
        System.out.println(dataSource);


    }
}
