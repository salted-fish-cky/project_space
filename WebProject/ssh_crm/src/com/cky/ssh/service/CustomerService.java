package com.cky.ssh.service;

import com.cky.ssh.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

public interface CustomerService {
    PageBean getPageBean(DetachedCriteria customer, Integer currentPage, Integer pageSize);
}
