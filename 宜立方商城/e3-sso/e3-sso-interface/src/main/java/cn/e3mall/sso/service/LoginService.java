package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {

    //参数：用户名和密码
    //业务逻辑
    /**
     * 1.判断用户和密码是否正确
     * 2.如果不正确，返回登录失败
     * 3.生成token
     * 4.吧用户信息写入redis，key：token，value：用户信息
     * 5.是指session的过期时间
     * 6.把token返回
     */
    E3Result userLogin(String username,String password);


}
