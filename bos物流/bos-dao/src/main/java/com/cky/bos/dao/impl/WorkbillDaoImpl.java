package com.cky.bos.dao.impl;

import com.cky.bos.dao.WorkbillDao;
import com.cky.bos.domain.Workbill;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkbillDaoImpl extends BaseDaoImpl<Workbill> implements WorkbillDao {
    public List<Workbill> findNewWorkbills() {
        String hql = "from Workbill w where w.type=?";
        return (List<Workbill>) this.getHibernateTemplate().find(hql,"新单");
    }
}
