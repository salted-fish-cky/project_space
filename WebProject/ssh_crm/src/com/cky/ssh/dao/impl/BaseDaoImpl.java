package com.cky.ssh.dao.impl;

import com.cky.ssh.dao.BaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 通用的Dao
 * @param <T>
 */

public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {
    private Class clazz;

    public BaseDaoImpl(){
        //获得当前类型带有泛型的父类
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获得运行时类的泛型
        clazz = (Class) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public void save(T t) {
        getHibernateTemplate().save(t);
    }

    @Override
    public void delete(T t) {
        getHibernateTemplate().delete(t);
    }

    @Override
    public void delete(Serializable id) {
        T t = this.getById(id);
        getHibernateTemplate().delete(t);
    }

    @Override
    public void update(T t) {
        getHibernateTemplate().update(t);
    }

    @Override
    public T getById(Serializable id) {
        return getHibernateTemplate().get((Class<T>) clazz,id);
    }

    @Override
    public Integer getTotalCount(DetachedCriteria criteria) {
        //设置查询的聚合函数
        criteria.setProjection(Projections.rowCount());
        List<Long> list = (List<Long>) getHibernateTemplate().findByCriteria(criteria);
        //清空之前的聚合函数
        criteria.setProjection(null);
        if(list!=null&&list.size()>0){
            Long count = list.get(0);
            return count.intValue();
        }
        return null;
    }

    @Override
    public List<T> getPageList(DetachedCriteria criteria, Integer start, Integer pageSize) {
        List<T> list = (List<T>) getHibernateTemplate().findByCriteria(criteria, start, pageSize);
        return list;
    }
}
