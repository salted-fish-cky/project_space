package com.cky.bos.interceptor;

import com.cky.bos.domain.User;
import com.cky.bos.utils.BOSUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class BOSLoginInterceptor extends MethodFilterInterceptor {
    //拦截方法
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        //从session中获取session对象
        User user = BOSUtils.getLoginUser();
        if(user == null){
            //没有登陆
            return "login";
        }
        //放行
        return actionInvocation.invoke();
    }
}
