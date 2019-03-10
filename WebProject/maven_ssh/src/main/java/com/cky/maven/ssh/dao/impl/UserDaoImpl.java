package com.cky.maven.ssh.dao.impl;

import com.cky.maven.ssh.dao.UserDao;
import com.cky.maven.ssh.entities.User;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {
    @Override
    public User findById(Integer id) {
        return this.getHibernateTemplate().get(User.class,id);
    }
}
