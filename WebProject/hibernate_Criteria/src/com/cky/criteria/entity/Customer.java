package com.cky.criteria.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {
    private int custId;
    private String custSource;
    private String custName;
    private String custIndustry;
    private String custLevel;
    private String custLinkman;
    private String custPhone;
    private String custMobile;

    @Id
    @Column(name = "cust_id", nullable = false)
    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    @Basic
    @Column(name = "cust_source", nullable = true, length = 255)
    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    @Basic
    @Column(name = "cust_name", nullable = true, length = 255)
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Basic
    @Column(name = "cust_industry", nullable = true, length = 255)
    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    @Basic
    @Column(name = "cust_level", nullable = true, length = 255)
    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    @Basic
    @Column(name = "cust_linkman", nullable = true, length = 255)
    public String getCustLinkman() {
        return custLinkman;
    }

    public void setCustLinkman(String custLinkman) {
        this.custLinkman = custLinkman;
    }

    @Basic
    @Column(name = "cust_phone", nullable = true, length = 255)
    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    @Basic
    @Column(name = "cust_mobile", nullable = true, length = 255)
    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (custId != customer.custId) return false;
        if (custSource != null ? !custSource.equals(customer.custSource) : customer.custSource != null) return false;
        if (custName != null ? !custName.equals(customer.custName) : customer.custName != null) return false;
        if (custIndustry != null ? !custIndustry.equals(customer.custIndustry) : customer.custIndustry != null)
            return false;
        if (custLevel != null ? !custLevel.equals(customer.custLevel) : customer.custLevel != null) return false;
        if (custLinkman != null ? !custLinkman.equals(customer.custLinkman) : customer.custLinkman != null)
            return false;
        if (custPhone != null ? !custPhone.equals(customer.custPhone) : customer.custPhone != null) return false;
        if (custMobile != null ? !custMobile.equals(customer.custMobile) : customer.custMobile != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = custId;
        result = 31 * result + (custSource != null ? custSource.hashCode() : 0);
        result = 31 * result + (custName != null ? custName.hashCode() : 0);
        result = 31 * result + (custIndustry != null ? custIndustry.hashCode() : 0);
        result = 31 * result + (custLevel != null ? custLevel.hashCode() : 0);
        result = 31 * result + (custLinkman != null ? custLinkman.hashCode() : 0);
        result = 31 * result + (custPhone != null ? custPhone.hashCode() : 0);
        result = 31 * result + (custMobile != null ? custMobile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custSource='" + custSource + '\'' +
                ", custName='" + custName + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custLinkman='" + custLinkman + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custMobile='" + custMobile + '\'' +
                '}';
    }
}
