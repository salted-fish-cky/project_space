package com.cky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cky","org.n3r.idworker"})
@PropertySource("classpath:source.properties")
@EnableTransactionManagement
public class WeixinAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinAdminApplication.class, args);
    }

}
