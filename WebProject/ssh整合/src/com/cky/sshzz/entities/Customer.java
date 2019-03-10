package com.cky.sshzz.entities;

import java.util.HashSet;
import java.util.Set;

public class Customer {
    private Integer custId;
    private String custName;
    private String custLinkman;
    private String custPhone;
    private String custMobile;
    private BaseDict custLevel;//客户等级
    private BaseDict custSource;//客户来源
    private  BaseDict custIndustry;//客户行业

    //引用关联的数据字典对象

    //表达客户与拜访记录一对多
    private Set<SaleVisit> saleVisits = new HashSet<>();

    public Set<SaleVisit> getSaleVisits() {
        return saleVisits;
    }

    public void setSaleVisits(Set<SaleVisit> saleVisits) {
        this.saleVisits = saleVisits;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustLinkman() {
        return custLinkman;
    }

    public void setCustLinkman(String custLinkman) {
        this.custLinkman = custLinkman;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public BaseDict getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(BaseDict custLevel) {
        this.custLevel = custLevel;
    }

    public BaseDict getCustSource() {
        return custSource;
    }

    public void setCustSource(BaseDict custSource) {
        this.custSource = custSource;
    }

    public BaseDict getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(BaseDict custIndustry) {
        this.custIndustry = custIndustry;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custLinkman='" + custLinkman + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custMobile='" + custMobile + '\'' +
                ", custLevel=" + custLevel +
                ", custSource=" + custSource +
                ", custIndustry=" + custIndustry +
                '}';
    }

}
