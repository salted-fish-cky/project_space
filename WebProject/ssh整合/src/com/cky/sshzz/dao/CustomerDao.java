package com.cky.sshzz.dao;

import com.cky.sshzz.entities.Customer;

import java.util.List;

public interface CustomerDao extends BaseDao<Customer> {
//    Integer getTotalCount(DetachedCriteria dc);
//
//    List<Customer> getPageList(DetachedCriteria dc, int start, Integer pageSize);
    List<Object[]> getIndustryCount();
}
