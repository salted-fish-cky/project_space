package com.cky.sshzz.action;

import com.cky.sshzz.entities.BaseDict;
import com.cky.sshzz.service.BaseDictService;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.util.List;


public class BaseDictAction extends ActionSupport {

    private String dict_type_code;

    private BaseDictService baseDictService;

    public void setDict_type_code(String dict_type_code) {
        this.dict_type_code = dict_type_code;
    }

    public void setBaseDictService(BaseDictService baseDictService) {
        this.baseDictService = baseDictService;
    }

    @Override
    public String execute() throws Exception {
        //1.调用service根据typecode获取数据字典
        List<BaseDict> list = baseDictService.getListByTypeCode(dict_type_code);
        //2.将list转换为json发送给浏览器
        Gson gson = new Gson();
        String json = gson.toJson(list);
        ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");

        ServletActionContext.getResponse().getWriter().write(json);

        return null;
//        return super.execute();
    }
}
