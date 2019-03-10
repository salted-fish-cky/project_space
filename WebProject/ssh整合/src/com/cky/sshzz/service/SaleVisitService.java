package com.cky.sshzz.service;

import com.cky.sshzz.entities.SaleVisit;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

public interface SaleVisitService {
    //保存客户拜访记录
    void save(SaleVisit saleVisit);

    PageBean getPageBean(DetachedCriteria dc, Integer currentPage, Integer pageSize);

    SaleVisit getById(String visitId);
}
