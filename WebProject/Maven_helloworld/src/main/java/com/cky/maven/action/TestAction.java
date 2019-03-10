package com.cky.maven.action;

import com.opensymphony.xwork2.ActionSupport;


public class TestAction extends ActionSupport{
    private int custId;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String findCustomerId(){

        System.out.println("id为："+custId);
        return SUCCESS;
    }
}
