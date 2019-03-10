package com.cky.spring.struts2.bean;

public class Person {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void hello(){
        System.out.println( "my name is"+username);
    }
}
