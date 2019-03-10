package com.cky.sshzz.dao.impl;

import com.cky.sshzz.dao.BaseDao;
import com.cky.sshzz.dao.BasedictDao;
import com.cky.sshzz.entities.BaseDict;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class BaseDictDaoImpl extends BaseDaoImpl<BaseDict> implements BasedictDao {
    @Override
    public List<BaseDict> getListByTypeCode(String dict_type_code) {
        //Criteria

        //创建离线查询对象
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BaseDict.class);
        //封装条件
        detachedCriteria.add(Restrictions.eq("dictTypeCode",dict_type_code));
        //执行查询
        List<BaseDict> list = (List<BaseDict>) getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }
}
