package com.cky.ssh.service.impl;

import com.cky.ssh.dao.CustomerDao;
import com.cky.ssh.entities.Customer;
import com.cky.ssh.service.CustomerService;
import com.cky.ssh.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

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
}
