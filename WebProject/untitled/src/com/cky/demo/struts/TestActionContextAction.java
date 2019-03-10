package com.cky.demo.struts;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.dispatcher.HttpParameters;

import java.util.Map;

/**
 * ActionContxt获取web资源
 */
public class TestActionContextAction {

    public String execute(){

        //获取ActionContext对象
        ActionContext actionContext = ActionContext.getContext();

        //1.获取aplication对应的map，并向其中添加属性
        Map<String,Object> applicationMap = actionContext.getApplication();
        applicationMap.put("applicationKey","applicationValue");
        //获取属性
        System.out.println("date"+applicationMap.get("date"));

        //2.session
        Map<String,Object> sessionMap = actionContext.getSession();
        sessionMap.put("sessionKey","sessionValue");
        //3.request
        //ActionContext 中并没有提供getRequest，
        //需要手工调用get（）方法，传入request字符串获取
        Map<String,Object> requestMap = (Map<String, Object>) actionContext.get("request");
        requestMap.put("requestKey","requestValue");
        //4.获取请求参数对应的map
        HttpParameters paramters = actionContext.getParameters();
        System.out.println(paramters.get("name"));

        //paramters 这个Map 只能读，不能写入数据，如果写入，不出错，但也不起作用
        return "success";
    }
}
