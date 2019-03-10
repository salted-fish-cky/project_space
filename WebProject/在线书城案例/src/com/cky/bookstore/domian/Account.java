package com.cky.bookstore.domian;

public class Account {
    private Integer accountId;
    private float balance;

    public Account(Integer accountId, float balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Account() {
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
