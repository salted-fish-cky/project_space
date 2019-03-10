package com.cky.sshzz.service.impl;

import com.cky.sshzz.dao.SaleVisitDao;
import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.SaleVisit;
import com.cky.sshzz.service.SaleVisitService;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public class SaleVisitServiceImpl implements SaleVisitService {
    private SaleVisitDao saleVisitDao;

    public void setSaleVisitDao(SaleVisitDao saleVisitDao) {
        this.saleVisitDao = saleVisitDao;
    }

    @Override
    public void save(SaleVisit saleVisit) {

        saleVisitDao.saveOrUpdate(saleVisit);
    }

    @Override
    public PageBean getPageBean(DetachedCriteria dc, Integer currentPage, Integer pageSize) {
        //1.调用dao查询总记录数
        Integer totalCount = saleVisitDao.getTotalCount(dc);
        //2.创建pageBean对象
        PageBean pb = new PageBean(currentPage,totalCount,pageSize);
        //3.调用dao查询分页列表数据
        List<SaleVisit> list = saleVisitDao.getPageList(dc,pb.getStart(),pb.getPageSize());
        //4.列表数据放入 pageBean中，并返回
        pb.setList(list);
        return pb;
    }

    @Override
    public SaleVisit getById(String visitId) {
        System.out.println(saleVisitDao.getById(visitId));
        return saleVisitDao.getById(visitId);
    }
}
