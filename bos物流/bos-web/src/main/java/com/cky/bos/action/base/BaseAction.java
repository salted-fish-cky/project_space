package com.cky.bos.action.base;

import com.cky.bos.utils.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    protected PageBean pageBean = new PageBean();
    DetachedCriteria detachedCriteria = null;
    protected Integer page;
    protected Integer rows;
    public static final String LOGIN = "login";
    public static final String HOME = "home";
    public static final String LIST = "list";

    public void setPage(Integer page) {

        pageBean.setCurrentPage(page);
    }


    public void setRows(Integer rows) {

        pageBean.setPageSize(rows);
    }

    public void java2Json(Object o,String[] exlueds){
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        JsonConfig jsonConfig = new JsonConfig();
//        指定哪些属性不需要转json
        jsonConfig.setExcludes(exlueds);
        String json = JSONObject.fromObject(o, jsonConfig).toString();
        ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void java2Json(List o, String[] exlueds){
        JsonConfig jsonConfig = new JsonConfig();
//        指定哪些属性不需要转json
        jsonConfig.setExcludes(exlueds);
        String json = JSONArray.fromObject(o, jsonConfig).toString();
        ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected T model ;
    public T getModel() {
        return model;
    }

    
    public BaseAction(){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        Class<T> entityClass = (Class<T>) actualTypeArguments[0];
        detachedCriteria = DetachedCriteria.forClass(entityClass);
        pageBean.setDetachedCriteria(detachedCriteria);
//        通过反射创建对象
        try {
            model = entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
