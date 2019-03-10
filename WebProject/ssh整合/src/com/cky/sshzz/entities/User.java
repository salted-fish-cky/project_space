package com.cky.sshzz.entities;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Integer userId;
    private String userCode;
    private String userName;
    private String userPassword;
    private String userState;

    private Set<SaleVisit> saleVisits = new HashSet<>();

    public Set<SaleVisit> getSaleVisits() {
        return saleVisits;
    }

    public void setSaleVisits(Set<SaleVisit> saleVisits) {
        this.saleVisits = saleVisits;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userState='" + userState + '\'' +
                '}';
    }
}
