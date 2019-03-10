package com.cky.spring.struts2.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        String configLocation = servletContext.getInitParameter("configLocation");

        //1.创建IOC容器
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
        //2.把IOC容器放在ServletContext 的一个属性中
        servletContext.setAttribute("applicationContext",context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
