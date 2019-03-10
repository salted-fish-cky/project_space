package com.cky.bos.service.impl;

import com.cky.bos.dao.SubAreaDao;
import com.cky.bos.domain.Subarea;
import com.cky.bos.service.SubAreaService;
import com.cky.bos.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
    @Autowired
    private SubAreaDao subAreaDao;
    public void save(Subarea model) {
        subAreaDao.save(model);
    }

    public void pageQuery(PageBean pageBean) {
        subAreaDao.pageQuery(pageBean);
    }

    public List<Subarea> findAll() {
        return subAreaDao.findAll();
    }

    public List<Subarea> findListNotAssociation() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subarea.class);
        detachedCriteria.add(Restrictions.isNull("decidedzone"));
        return subAreaDao.findByCriteria(detachedCriteria);
    }

    /**
     * 根据定区id查询关联的分区
     * @param decidedzoneId
     * @return
     */
    public List<Subarea> findListByDecidedzoneId(String decidedzoneId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subarea.class);
        detachedCriteria.add(Restrictions.eq("decidedzone.id",decidedzoneId));
        return subAreaDao.findByCriteria(detachedCriteria);
    }

    public List<Object> findSubAreasGroupByProvince() {
        return subAreaDao.findSubAreasGroupByProvince();
    }
}
