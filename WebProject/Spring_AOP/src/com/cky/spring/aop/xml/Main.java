package com.cky.spring.aop.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args){
        //1.创建spring的IOC容器
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config-xml.xml");
        //2.从IOC容器中获取bean的实例
        AtithmeticCalculator atithmeticCalculator = context.getBean(AtithmeticCalculator.class);

        //3.使用bean
        int result = atithmeticCalculator.div(4,2);
        System.out.println(result);
    }

}
