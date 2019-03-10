package com.cky.bos.dao.impl;

import com.cky.bos.dao.FunctionDao;
import com.cky.bos.domain.Function;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FunctionDaoImpl extends BaseDaoImpl<Function> implements FunctionDao {


    public List<Function> findAll(){
        String hql = "from Function f where f.parentFunction is null ";
        return (List<Function>) this.getHibernateTemplate().find(hql);
    }

    public List<Function> findFunctionListByUserId(String id) {
        String hql = "select distinct f from Function f left outer join f.roles r left outer join r.users u where u.id=?";
        List<Function> list = (List<Function>) this.getHibernateTemplate().find(hql, id);
        return list;
    }

    public List<Function> findAllMenu() {
        String hql = "from Function f where f.generatemenu = '1' order by f.zindex";
        return (List<Function>) this.getHibernateTemplate().find(hql);
    }

    public List<Function> findMenuByUserId(String id) {
        String hql = "select distinct f from Function f left outer join f.roles r left outer join r.users u where u.id=? and f.generatemenu = '1' order by f.zindex";
        List<Function> list = (List<Function>) this.getHibernateTemplate().find(hql, id);
        return list;
    }
}
