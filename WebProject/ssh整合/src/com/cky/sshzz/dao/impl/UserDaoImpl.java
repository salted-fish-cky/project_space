package com.cky.sshzz.dao.impl;


import com.cky.sshzz.dao.UserDao;
import com.cky.sshzz.entities.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    @Override
    public User getByUserCode(String userCode) {
//        return (User) getHibernateTemplate().find("from User u where u.userCode=?",userCode);
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.eq("userCode",userCode));
        List<User> users = (List<User>) getHibernateTemplate().findByCriteria(dc);
        User user=null;
        if(users==null||users.size()==0){
            return user;
        }
        return users.get(0);
    }
}
