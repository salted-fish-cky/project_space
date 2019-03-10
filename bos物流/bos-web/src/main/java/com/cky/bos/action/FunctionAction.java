package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Function;
import com.cky.bos.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class FunctionAction extends BaseAction<Function> {

    @Autowired
    private FunctionService functionService;
    public String listAjax(){
        List<Function> list =  functionService.findAll();
        java2Json(list,new String[] {"parentFunction","roles"});
        return NONE;
    }


    public String add(){
        functionService.save(model);
        return LIST;
    }

    public String pageQuery(){
        pageBean.setCurrentPage(Integer.parseInt(model.getPage()));
        functionService.pageQuery(pageBean);
        java2Json(pageBean,new String[]{"parentFunction","children","roles","currentPage","detachedCriteria","pageSize"});
        return NONE;
    }

    public String findMenu(){
        List<Function> list = functionService.findMenu();
        java2Json(list,new String[]{"parentFunction","children","roles"});
        return NONE;
    }
}
