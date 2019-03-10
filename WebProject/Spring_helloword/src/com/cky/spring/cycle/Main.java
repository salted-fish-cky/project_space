package com.cky.spring.cycle;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans-cycle.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car);

        //关闭IOC容器
        context.close();
    }
}
