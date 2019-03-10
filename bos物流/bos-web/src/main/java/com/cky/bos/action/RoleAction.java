package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Role;
import com.cky.bos.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {
    @Autowired
    private RoleService roleService;

    private String functionIds;

    public void setFunctionIds(String functionIds) {
        this.functionIds = functionIds;
    }

    public String add(){
        roleService.save(model,functionIds);
        return LIST;
    }

    /**
     * 分页查询
     * @return
     */
    public String pageQuery(){
        roleService.pageQuery(pageBean);
        this.java2Json(pageBean,new String[]{"currentPage","detachedCriteria","pageSize","functions","users"});
        return NONE;
    }

    public String listAjax(){
        List<Role>  list =  roleService.findAll();
        java2Json(list,new String[]{"functions","users"});
        return NONE;
    }
}
