package com.cky.demo.struts2;

import com.opensymphony.xwork2.ActionSupport;

import java.util.Date;

/**
 * 要显示错误消息要继承ActionSupport
 *
 *
 * 1.覆盖错误消息，在对应的Action类所在的包中新建ActionClassName。properties文件，
 * ActionClassName即为包含这输入字段的Action类
 * 2.在属性文件中添加如下键值对：invalid.filedvalue.filedName=xxx
 */
public class ConversionAction extends ActionSupport{
    private int age;
    private Date birth;

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setAge(int age) {
        this.age = age;

    }
    public int getAge() {
        return age;
    }

    public String execute(){
        System.out.println(age);
        return "success";
    }
}

