package com.cky.bos.domain;

import java.util.HashSet;
import java.util.Set;

public class Function {
    private String id;
    private String name;
    private String code;
    private String description;
    private String page;
    private String generatemenu;
    private Integer zindex;
    private Function parentFunction;
    private Set<Function> children = new HashSet<Function>();
    private Set<Role> roles = new HashSet<Role>();


    public String getpId(){
        if(parentFunction!=null){
            return parentFunction.getId();
        }
        return "0";
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getText(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getGeneratemenu() {
        return generatemenu;
    }

    public void setGeneratemenu(String generatemenu) {
        this.generatemenu = generatemenu;
    }

    public Integer getZindex() {
        return zindex;
    }

    public void setZindex(Integer zindex) {
        this.zindex = zindex;
    }



    public Function getParentFunction() {
        return parentFunction;
    }

    public void setParentFunction(Function parentFuction) {
        this.parentFunction = parentFuction;
    }

    public Set<Function> getChildren() {
        return children;
    }

    public void setChildren(Set<Function> children) {
        this.children = children;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
