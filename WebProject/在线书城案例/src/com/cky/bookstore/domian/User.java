package com.cky.bookstore.domian;

import java.util.LinkedHashSet;
import java.util.Set;

public class User {

    private Integer userId;
    private String username;
    private int accountId;

    private Set<Trade> trades = new LinkedHashSet<>();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Set<Trade> getTrades() {
        return trades;
    }

    public void setTrades(Set<Trade> trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", accountId=" + accountId +
                ", trades=" + trades +
                '}';
    }
}
