package com.cky.struts2.validation;

import com.opensymphony.xwork2.ActionSupport;

public class TestValidationAction extends ActionSupport{

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }
}
