package com.cky.demo.struts;


import org.apache.struts2.interceptor.ApplicationAware;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Aware获取web资源
 */
public class TestAwareAction implements ApplicationAware,SessionAware,RequestAware{

    private Map<String, Object> application;
    private Map<String, Object> session;
    private Map<String, Object> request;
//    private Map<String, String[]> parameters;
    public String execute(){

        application.put("applicationKey","applicationValue");
        session.put("sessionKey","sessionValue");
        request.put("requestKey","requestValue");
//        System.out.println(parameters.get("name"));
        return "success";
    }


    @Override
    public void setApplication(Map<String, Object> application) {
        this.application = application;
    }

    @Override
    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}
