package com.cky.sshzz.interceptor;

import com.cky.sshzz.entities.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import java.util.Map;

public class PrivilegeInterceptor extends MethodFilterInterceptor {

    /**
     * 不校验注册登录方法
     * @param invocation
     * @return
     * @throws Exception
     */
    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        //1.获得session
        Map<String, Object> session = ActionContext.getContext().getSession();
        //2.获得登陆标识
        User user = (User) session.get("user");

        //3.判断标识是否存在
        if(user!=null){
            //存在=>放行
            return invocation.invoke();
        }else{
            //不存在=> 重定向到登陆界面

            return "toLogin";
        }
        //4.

    }
}
