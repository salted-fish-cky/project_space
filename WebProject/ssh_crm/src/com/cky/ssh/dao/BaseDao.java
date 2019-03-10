package com.cky.ssh.dao;

import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T>{

    //增
    void save(T t);
    //删
    void delete(T t);
    //删
    void delete(Serializable id);
    //改
    void update(T t);
    //查 根据id查询
    T getById(Serializable id);
    //查 符合条件的总记录数
    Integer getTotalCount(DetachedCriteria criteria);
    //查 查询分页列表
    List<T> getPageList(DetachedCriteria criteria,Integer start,Integer pageSize);
}
