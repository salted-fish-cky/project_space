package com.cky.spring.aop.impl;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 *把这个类声明为一个切面：需要把该类放入daoIOC容器中，再声明一个切面
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * 定义一个方法，用于声明切入点表达式，一般该方法中再不需要添入其他的代码
     */
    @Pointcut("execution(* com.cky.spring.aop.impl.AtithmeticCalculator.*(..))")
    public void declareJointPointExpressin(){

    }

    //声明 该方法是一个前置通知，在目标方法开始之前执行
    @Before("declareJointPointExpressin()")
    public void beforeMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> objects = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method "+methodName+" begins"+objects);
    }

    //后置通知：在目标方法执行后（无论是否发生异常），执行通知
    //在后置通知中还不能访问目标方法执行的结果
    @After("declareJointPointExpressin()")
    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends");
    }

    /**
     * 在方法正常结束后执行的代码
     * 返回通知是可以访问到方法的返回值
     * @param joinPoint
     */
    @AfterReturning(value = "declareJointPointExpressin()",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends with "+result);
    }

    /**
     * 在目标方法出现异常是会执行的代码，
     * 可以访问到异常对象；且可以指定在出现特定异常是在执行通知代码
     * @param e
     * @param joinPoint
     */
    @AfterThrowing(value = "declareJointPointExpressin()",throwing = "e")
    public void afterThowing(JoinPoint joinPoint,Exception e){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("The method "+methodName+" ends with "+e);
    }
}
