package com.cky.bos.dao.impl;

import com.cky.bos.dao.UserDao;
import com.cky.bos.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    public User findByUserNameAndPassword(String username, String password) {
        String hql = "from User user where user.username=? and user.password=?";
        List<User> list = (List<User>) getHibernateTemplate().find(hql, username, password);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public User findUserByUsername(String username) {
        String hql = "from User u where u.username=?";
        List<User> list = (List<User>) getHibernateTemplate().find(hql, username);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
