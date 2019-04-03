package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:dubbo/dubbo-provider.xml")
public class ProvidercApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvidercApplication.class, args);
    }

}
