package com.cky.sshzz.entities;

public class LinkMan {
    private Integer lkmId;
    private Character lkmGender;
    private String lkmName;
    private String lkmPhone;
    private String lkmEmail;
    private String lkmQq;
    private String lkmMemo;
    private String lkmPosition;
//    private Integer lkmCustId;
    private Customer customer;

    public Integer getLkmId() {
        return lkmId;
    }

    public void setLkmId(Integer lkmId) {
        this.lkmId = lkmId;
    }

    public Character getLkmGender() {
        return lkmGender;
    }

    public void setLkmGender(Character lkmGender) {
        this.lkmGender = lkmGender;
    }

    public String getLkmName() {
        return lkmName;
    }

    public void setLkmName(String lkmName) {
        this.lkmName = lkmName;
    }

    public String getLkmPhone() {
        return lkmPhone;
    }

    public void setLkmPhone(String lkmPhone) {
        this.lkmPhone = lkmPhone;
    }

    public String getLkmEmail() {
        return lkmEmail;
    }

    public void setLkmEmail(String lkmEmail) {
        this.lkmEmail = lkmEmail;
    }

    public String getLkmQq() {
        return lkmQq;
    }

    public void setLkmQq(String lkmQq) {
        this.lkmQq = lkmQq;
    }

    public String getLkmMemo() {
        return lkmMemo;
    }

    public void setLkmMemo(String lkmMemo) {
        this.lkmMemo = lkmMemo;
    }

    public String getLkmPosition() {
        return lkmPosition;
    }

    public void setLkmPosition(String lkmPosition) {
        this.lkmPosition = lkmPosition;
    }

//    public Integer getLkmCustId() {
//        return lkmCustId;
//    }
//
//    public void setLkmCustId(Integer lkmCustId) {
//        this.lkmCustId = lkmCustId;
//    }



    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "LinkMan{" +
                "lkmId=" + lkmId +
                ", lkmGender=" + lkmGender +
                ", lkmName='" + lkmName + '\'' +
                ", lkmPhone='" + lkmPhone + '\'' +
                ", lkmEmail='" + lkmEmail + '\'' +
                ", lkmQq='" + lkmQq + '\'' +
                ", lkmMemo='" + lkmMemo + '\'' +
                ", lkmPosition='" + lkmPosition + '\'' +
                ", customer=" + customer +
                '}';
    }
}
