package com.cky.sshzz.service.impl;


import com.cky.sshzz.entities.Customer;
import org.hibernate.criterion.DetachedCriteria;
import com.cky.sshzz.dao.CustomerDao;
import com.cky.sshzz.service.CustomerService;
import com.cky.sshzz.utils.PageBean;

import java.util.List;

public class CustomerServiceImpl  implements CustomerService {

    private CustomerDao cd;

    public void setCd(CustomerDao cd) {
        this.cd = cd;
    }

    @Override
    public PageBean getPageBean(DetachedCriteria dc, Integer currentPage, Integer pageSize) {
       //1.调用dao查询总记录数
        Integer totalCount = cd.getTotalCount(dc);
        //2.创建pageBean对象
        PageBean pb = new PageBean(currentPage,totalCount,pageSize);
        //3.调用dao查询分页列表数据
        List<Customer> list = cd.getPageList(dc,pb.getStart(),pb.getPageSize());
        //4.列表数据放入 pageBean中，并返回
        pb.setList(list);
        return pb;
    }

    @Override
    public void save(Customer customer) {
        //1.维护Customer与数据字典的关系

        //2.调用Dao保存客户
        cd.saveOrUpdate(customer);
    }

    @Override
    public Customer getById(Integer custId) {
        return cd.getById(custId);
    }

    @Override
    public List<Object[]> getIndustryCount() {

        return cd.getIndustryCount();
    }
}
