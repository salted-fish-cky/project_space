package com.cky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;


//@ComponentScan(basePackages = {"com.cky.springboot.demo",
//        "com.cky.springboot.service","com.cky.springboot.controller"})
@MapperScan(basePackages = {"com.cky.mapper"})
@ComponentScan(basePackages = {"com.cky","org.n3r.idworker"})
@PropertySource("classpath:resource-prod.properties")
@SpringBootApplication
public class App extends SpringBootServletInitializer {

    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }

    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
