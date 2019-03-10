package com.cky.springmvc.crud.entities;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Employee {

    private Integer id;
    @NotEmpty
    private String name;
    @Email
    private String email;
    private int sex;
    private Department department;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee(Integer id, String name, String email, int sex, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.department = department;
    }

    public Employee() {
    }
}