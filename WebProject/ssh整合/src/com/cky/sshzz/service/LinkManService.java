package com.cky.sshzz.service;

import com.cky.sshzz.entities.LinkMan;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

public interface LinkManService {

    //保存联系人
    void save(LinkMan linkMan);


    PageBean getPageBean(DetachedCriteria customer, Integer currentPage, Integer pageSize);

    LinkMan getById(Integer lkmId);
}
