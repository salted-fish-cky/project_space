package com.cky.sshzz.service.impl;

import com.cky.sshzz.dao.LinkManDao;
import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.LinkMan;
import com.cky.sshzz.service.LinkManService;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public class LinkManServiceImpl implements LinkManService {

    private LinkManDao linkManDao;

    public void setLinkManDao(LinkManDao linkManDao) {
        this.linkManDao = linkManDao;
    }

    @Override
    public void save(LinkMan linkMan) {
        System.out.println(linkMan);
        linkManDao.saveOrUpdate(linkMan);
    }

    @Override
    public PageBean getPageBean(DetachedCriteria dc, Integer currentPage, Integer pageSize) {
        //1.调用dao查询总记录数
        Integer totalCount = linkManDao.getTotalCount(dc);
        //2.创建pageBean对象
        PageBean pb = new PageBean(currentPage,totalCount,pageSize);
        //3.调用dao查询分页列表数据
        List<LinkMan> list = linkManDao.getPageList(dc,pb.getStart(),pb.getPageSize());
        //4.列表数据放入 pageBean中，并返回
        pb.setList(list);
        return pb;
    }

    @Override
    public LinkMan getById(Integer lkmId) {

        System.out.println(linkManDao.getById(lkmId));
        return linkManDao.getById(lkmId);
    }
}
