package com.cky.bos.dao;

import com.cky.bos.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * 持久层通用接口
 */
public interface BaseDao<T> {
    public void save(T entity);
    public void delete(T entity);
    public void update(T entity);
    public void saveOrUpdate(T entity);
    public T findById(Serializable id);
    public List<T> findByCriteria(DetachedCriteria detachedCriteria);
    public List<T> findAll();
    public void executeUpdate(String queryName,Object...objects);
    public void pageQuery(PageBean pageBean);
}
