package com.cky.spring.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-properties.xml");

//        HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
//        helloWorld.sayHello();
//
//        Car car1 = (Car) context.getBean("car1");
//        System.out.println(car1);
//        Person person = (Person) context.getBean("person");
//        System.out.println(person);


         DataSource dataSource = (DataSource) context.getBean("dataSource");
        System.out.println(dataSource.getConnection());
    }
}
