package com.cky.bos.service.realm;

import com.cky.bos.dao.FunctionDao;
import com.cky.bos.dao.UserDao;
import com.cky.bos.domain.Function;
import com.cky.bos.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BOSRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FunctionDao functionDao;

    //认证方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获得页面输入的用户名
        UsernamePasswordToken passwordToken = (UsernamePasswordToken) authenticationToken;
        String username = passwordToken.getUsername();

        //根据用户名查询数据库中的密码
        User user = userDao.findUserByUsername(username);

        if(user == null){
            //页面输入的用户名不存在
            return null;
        }

        //框架负责比对数据库中的密码和页面输入的密码是否一致
        AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
        return info;
    }
    //授权方法

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //为用户授权
        // 获得当前登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Function> list = null;
        //根据当前登陆用户查询数据库，获取实际对应的权限
        if(user.getUsername().equals("admin")){
            //超级管理员内置用户，查询所有权限数据
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Function.class);

            list =   functionDao.findByCriteria(detachedCriteria);
        }else{
            list = functionDao.findFunctionListByUserId(user.getId());
        }
        for (Function function:list){
            info.addStringPermission(function.getCode());
        }
        return info;
    }
}
