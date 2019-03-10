package com.cky.bos.dao.impl;

import com.cky.bos.dao.SubAreaDao;
import com.cky.bos.domain.Subarea;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubAreaDaoImpl extends BaseDaoImpl<Subarea> implements SubAreaDao {
    public List<Object> findSubAreasGroupByProvince() {
        String hql = "select r.province,count(*) from Subarea s left outer join s.region r group by r.province";
        return (List<Object>) this.getHibernateTemplate().find(hql);
    }
}
