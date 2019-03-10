package com.cky.sshzz.service;


import com.cky.sshzz.entities.Customer;
import org.hibernate.criterion.DetachedCriteria;
import com.cky.sshzz.utils.PageBean;

import java.util.List;

public interface CustomerService {
    PageBean getPageBean(DetachedCriteria customer, Integer currentPage, Integer pageSize);

    void save(Customer customer);

    Customer getById(Integer custId);

    List<Object[]> getIndustryCount();
}
