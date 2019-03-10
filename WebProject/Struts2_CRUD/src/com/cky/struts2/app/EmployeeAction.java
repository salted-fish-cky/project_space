package com.cky.struts2.app;

import com.cky.struts2.bean.Dao;
import org.apache.struts2.interceptor.RequestAware;

import java.util.Map;

public class EmployeeAction implements RequestAware{

    private Integer employeeId;
    private Dao dao = new Dao();

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String list(){
        requestMap.put("employees",dao.getEmployees());
        return "list";
    }

    public String delete(){
        dao.delete(employeeId);
        return "delete";
    }

    private Map<String,Object> requestMap;
    @Override
    public void setRequest(Map<String, Object> request) {
        requestMap = request;

    }
}
