package com.cky.bos.service.impl;

import com.cky.bos.dao.StaffDao;
import com.cky.bos.domain.Staff;
import com.cky.bos.service.StaffService;
import com.cky.bos.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService{
    @Autowired
    private StaffDao staffDao;
    public void add(Staff model) {
        staffDao.save(model);
    }

    public void pageQuery(PageBean pageBean) {
        staffDao.pageQuery(pageBean);
    }

    public List<Staff> findListNotDelete() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Staff.class);
        //添加过滤条件，deltag等于0
        detachedCriteria.add(Restrictions.eq("deltag","0"));
        return staffDao.findByCriteria(detachedCriteria);
    }

    public void deleteBatch(List<String> ids) {
        for(String id : ids){
            staffDao.executeUpdate("staff.delete",id);
        }
    }
}
