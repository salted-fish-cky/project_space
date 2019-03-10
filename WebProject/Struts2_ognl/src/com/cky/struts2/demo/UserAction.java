package com.cky.struts2.demo;

import java.util.Arrays;
import java.util.List;

public class UserAction {

    private String userId;
    private String userName;
    private String password;
    private String desc;
    private boolean married;
    private List<String> city;
    private String age;
    private String gender;


    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public boolean isMarried() {
        return married;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String save(){
        System.out.println(this);
        return "input";
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", desc='" + desc + '\'' +
                ", married=" + married +
                ", city=" + city +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
