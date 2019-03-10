package com.cky.bos.service.impl;

import com.cky.bos.dao.UserDao;
import com.cky.bos.domain.Role;
import com.cky.bos.domain.User;
import com.cky.bos.service.UserService;
import com.cky.bos.utils.MD5Utils;
import com.cky.bos.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional   //事务注解
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User login(User model) {
        User user = userDao.findByUserNameAndPassword(model.getUsername(),MD5Utils.md5(model.getPassword()));
        return  user;
    }

    /**
     * 根据用户id修改密码
     * @param id
     * @param password
     */
    public void editPassword(String id, String password) {
        password = MD5Utils.md5(password);
        userDao.executeUpdate("user.editPassword",password,id);
    }

    public void save(User user, String[] roleIds) {
        String password = user.getPassword();
        password = MD5Utils.md5(password);
        user.setPassword(password);
        userDao.save(user);
        if(roleIds!=null&&roleIds.length>0){
            for (String roleId:roleIds){
                Role role = new Role();
                role.setId(roleId);
                user.getRoles().add(role);
            }
        }
    }

    public void pageQuery(PageBean pageBean) {
        userDao.pageQuery(pageBean);
    }
}
