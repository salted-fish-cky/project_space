package com.cky.sshzz.service.impl;

import com.cky.sshzz.dao.UserDao;
import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.User;
import com.cky.sshzz.service.UserService;
import com.cky.sshzz.utils.MD5Utils;
import com.cky.sshzz.utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void saveUser(User user) {
        User u = userDao.getByUserCode(user.getUserCode());
        if(u!=null){

            throw new RuntimeException("用户名已经存在！");
        }
        //设置MD5文件加密
        user.setUserPassword(MD5Utils.md5(user.getUserPassword()));
        userDao.save(user);
    }

    @Override
    public User getUserByCodePassword(User user) {
        User u = userDao.getByUserCode(user.getUserCode());
        if(u==null){
            throw new RuntimeException("用户不存在！");
        }else{
            if(!u.getUserPassword().equals(MD5Utils.md5(user.getUserPassword()))){
                throw  new RuntimeException("密码错误！");
            }else{

                return u;
            }

        }


    }

    @Override
    public PageBean getPageBean(DetachedCriteria dc, Integer page, Integer rows) {
        //1.调用dao查询总记录数
        Integer totalCount = userDao.getTotalCount(dc);
        //2.创建pageBean对象
        PageBean pb = new PageBean(page,totalCount,rows);
        //3.调用dao查询分页列表数据
        List<User> list = userDao.getPageList(dc,pb.getStart(),pb.getPageSize());
        //4.列表数据放入 pageBean中，并返回
        pb.setList(list);
        return pb;
    }
}
