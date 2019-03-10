package com.cky.springmvc;

import org.springframework.web.bind.annotation.RequestMapping;

public class Helloworld {


    public Helloworld(){

        System.out.println("helloworld Constructor");
    }

    @RequestMapping("/helloworld")
    public String hello(){

        System.out.println("success");
        return "success";
    }
}
