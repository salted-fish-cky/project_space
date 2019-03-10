package com.cky.sshzz.entities;

import com.alibaba.fastjson.annotation.JSONField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleVisit {
    private String visitId;
    private Date visitTime;
    private String visitInterviewwee;
    private String visitAddress;
    private String visitDetail;
    private java.util.Date visitNexttime;
    @JSONField(serialize = false)
    private User user;
    @JSONField(serialize = false)
    private Customer customer;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {

        if(visitId.equals("")){
            visitId=null;
        }
        this.visitId = visitId;
    }

    public java.util.Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(java.util.Date visitTime) {
        this.visitTime = visitTime;
    }

    public String getVisitInterviewwee() {
        return visitInterviewwee;
    }

    public void setVisitInterviewwee(String visitInterviewwee) {
        this.visitInterviewwee = visitInterviewwee;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getVisitDetail() {
        return visitDetail;
    }

    public void setVisitDetail(String visitDetail) {
        this.visitDetail = visitDetail;
    }

    public java.util.Date getVisitNexttime() {
        return visitNexttime;
    }

    public void setVisitNexttime(java.util.Date visitNexttime) {
        this.visitNexttime = visitNexttime;
    }


    public String getVisitTime_s(){
        if(visitTime == null){
            return "";
        }
        return transferDate(visitTime,"yyyy-MM-dd");
    }

    public String getVisitNexttime_s(){
        if(visitTime == null){
            return "";
        }
        return transferDate(visitNexttime,"yyyy-MM-dd");
    }

    public static String transferDate(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    @Override
    public String toString() {
        return "SaleVisit{" +
                "visitId='" + visitId + '\'' +
                ", visitTime=" + visitTime +
                ", visitInterviewwee='" + visitInterviewwee + '\'' +
                ", visitAddress='" + visitAddress + '\'' +
                ", visitDetail='" + visitDetail + '\'' +
                ", visitNexttime=" + visitNexttime +
                ", user=" + user +
                ", customer=" + customer +
                '}';
    }
}
