package com.cky.struts2.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dao {

    private static Map<Integer,Employee> employees = new LinkedHashMap<>();

    static {
        employees.put(1001,new Employee(1001,"AA","aa","aa@qq.com"));
        employees.put(1002,new Employee(1002,"BB","bb","bb@qq.com"));
        employees.put(1003,new Employee(1003,"CC","cc","cc@qq.com"));
        employees.put(1004,new Employee(1004,"DD","dd","dd@qq.com"));
        employees.put(1005,new Employee(1005,"EE","ee","ee@qq.com"));
    }

    public List<Employee> getEmployees(){
        return new ArrayList<>(employees.values());

    }
    public void delete(Integer id){
        employees.remove(id);
    }
    public void save(Employee employee){
        Integer time  = Math.toIntExact(System.currentTimeMillis());
        employee.setEmployeeId(time);
        employees.put(employee.getEmployeeId(),employee);
    }

    public Employee get(Integer id){
        return employees.get(id);
    }

    public void update(Employee employee){
        employees.put(employee.getEmployeeId(),employee);
    }
}
