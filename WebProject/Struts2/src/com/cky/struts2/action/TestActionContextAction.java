package com.cky.struts2.action;

import com.opensymphony.xwork2.ActionContext;

import java.util.HashMap;
import java.util.Map;

public class TestActionContextAction {

    public String execute(){

        //获取ActionContext对象
        ActionContext actionContext = ActionContext.getContext();

        //1.获取aplication对应的map，并向其中添加属性
        Map<String,Object> applicationMap = actionContext.getApplication();
        applicationMap.put("applicationKey","applicationValue");
        //2.session

        //3.request

        //4.获取请求参数对应的map

        return "sucess";
    }
}
