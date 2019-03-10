package com.cky.demo.struts;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class UserAction implements SessionAware,ApplicationAware {

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 退出登录
     * @return
     */
    public String logout(){
        //1.人数-1
        Integer count = (Integer) application.get("count");
        if(count!=null&&count>0){
            count--;
            application.put("count",count);
        }
        //2.session失效
        ((SessionMap)session).invalidate();
        return "logout-success";
    }

    /**
     * 登入
     * @return
     */
    public String execute(){
        //把用户信息存入Session域中
        session.put("username",username);
        //获取登录信息

        //在线人数加1
        Integer count = (Integer) application.get("count");
        if(count == null){
            count = 0;
        }
        count++;
        application.put("count",count);
        return "login-success";
    }

    private Map<String,Object> session ;
    private Map<String, Object> application;
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    @Override
    public void setApplication(Map<String, Object> application) {
        this.application = application;
    }
}
