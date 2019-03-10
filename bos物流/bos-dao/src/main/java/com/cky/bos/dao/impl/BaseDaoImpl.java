package com.cky.bos.dao.impl;

import com.cky.bos.dao.BaseDao;
import com.cky.bos.utils.PageBean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 持久层实现类
 * @param <T>
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {
    private Class<T> clazz;

    @Autowired
    public void setMySessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    public BaseDaoImpl(){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获得父类上声明的泛型数组
        Type[] actualTypeArguments = type.getActualTypeArguments();
        clazz = (Class<T>) actualTypeArguments[0];
    }

    public void save(T entity) {
        this.getHibernateTemplate().save(entity);
    }

    public void delete(T entity) {
        this.getHibernateTemplate().delete(entity);
    }

    public void update(T entity) {
        this.getHibernateTemplate().update(entity);
    }

    public void saveOrUpdate(T entity) {
        this.getHibernateTemplate().saveOrUpdate(entity);
    }

    public T findById(Serializable id) {
        return this.getHibernateTemplate().get(clazz,id);
    }

    public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
        return (List<T>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    public List<T> findAll() {
        String hql = "from "+clazz.getSimpleName();

        return (List<T>) this.getHibernateTemplate().find(hql);
    }

    public void executeUpdate(String queryName, Object... objects) {
        Session session = this.getSessionFactory().getCurrentSession();
        //在映射文件里声明hql语句要用 getNamedQeury方法
        Query query = session.getNamedQuery(queryName);
        int i = 0;
        for(Object object:objects){
            query.setParameter(i,object);
            i++;
        }
        query.executeUpdate();
    }

    /**
     *通用分页查询方法
     * @param pageBean
     */
    public void pageQuery(PageBean pageBean) {
        Integer currentPage = pageBean.getCurrentPage();
        Integer pageSize = pageBean.getPageSize();

        DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
        //查询total -- 总数量
        detachedCriteria.setProjection(Projections.rowCount());
        List<Long> countList = (List<Long>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
        long count = countList.get(0);
        pageBean.setTotal((int) count);

        //查询rows -- 当前页需要展示的集合
        detachedCriteria.setProjection(null);
        //指定hibernate框架封装对像的方式
        detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
        int firstResult = (currentPage-1)*pageSize;
        int maxResult = pageSize;
        List rows = this.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult, maxResult);
        pageBean.setRows(rows);
    }
}
