package com.cky.spring.aop.helloworld;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ArithmeticCalculatorLoggingProxy {
    //要代理的对象
    private AtithmeticCalculator target;

    public ArithmeticCalculatorLoggingProxy(AtithmeticCalculator target){
        this.target = target;
    }

    public AtithmeticCalculator getLoggingProxy(){
        AtithmeticCalculator proxy = null;

        //代理对象由哪一个类加载器负责加载
        ClassLoader loader = target.getClass().getClassLoader();
        //代理对象的类型，即其中有哪些方法
        Class[] interfaces = new Class[]{AtithmeticCalculator.class};
        //当调用代理对象其中的方法时，该执行的代码
        InvocationHandler h = new InvocationHandler() {
            /**
             * proxy:正在返回的那个代理对象，一般情况下，在invoke方法中都不使用该对象
             * method：正在被调用的方法
             * args：调用方法时，传入的参数
             * @param o
             * @param method
             * @param objects
             * @return
             * @throws Throwable
             */
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                System.out.println("invoke...");
                String methodName = method.getName();
                //日志
                System.out.println("The method "+methodName+"begins with"+ Arrays.asList(objects));
                //执行方法
                Object result = method.invoke(target,objects);
                return result;
            }
        };
        proxy = (AtithmeticCalculator) Proxy.newProxyInstance(loader,interfaces,h);

        return proxy;
    }
}
