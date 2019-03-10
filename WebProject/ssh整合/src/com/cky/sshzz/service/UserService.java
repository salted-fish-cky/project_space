package com.cky.sshzz.service;

import com.cky.sshzz.entities.User;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

public interface UserService {
    void saveUser(User user);

    User getUserByCodePassword(User user);

    PageBean getPageBean(DetachedCriteria dc, Integer page, Integer rows);
}
