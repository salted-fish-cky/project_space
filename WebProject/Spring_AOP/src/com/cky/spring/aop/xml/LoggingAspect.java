package com.cky.spring.aop.xml;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 *把这个类声明为一个切面：需要把该类放入daoIOC容器中，再声明一个切面
 */

public class LoggingAspect {



    public void beforeMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> objects = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method "+methodName+" begins"+objects);
    }


    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends");
    }

    public void afterReturning(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends with "+result);
    }


    public void afterThowing(JoinPoint joinPoint,Exception e){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends with "+e);
    }
}
