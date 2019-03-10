package com.cky.sshzz.action;

import com.alibaba.fastjson.JSON;
import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.User;
import com.cky.sshzz.service.UserService;
import com.cky.sshzz.utils.PageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserAction extends ActionSupport implements ModelDriven<User>{

    private User user = new User();

    private UserService userService;

    private Integer page;

    private Integer rows;



    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String login(){
        User u=null;
        try{

             u = userService.getUserByCodePassword(user);
        }catch (Exception e){
            e.printStackTrace();
            ActionContext.getContext().put("error",e.getMessage());
            return "login";
        }

        System.out.println(u);
        //将返回的user对象放入到session域
        ActionContext.getContext().getSession().put("user",u);
        return "toHome";
    }

    public String regist(){
        System.out.println(user);
        try{

            userService.saveUser(user);
        }catch (Exception e){
            e.printStackTrace();
            ActionContext.getContext().put("error",e.getMessage());
            return "regist";
        }
        return "toLogin";
    }


    public String list() throws IOException {

        System.out.println("hehe");
        //封装离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);

        //1.调用service查询分页数据
        PageBean pb = userService.getPageBean(dc,page,rows);

        //将列表的数据转换为json发送给浏览器
        Map map = new HashMap();
        map.put("total",pb.getTotalCount());
        map.put("rows",pb.getList());

            String json = JSON.toJSONString(map);
            System.out.println(json);
            ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
            ServletActionContext.getResponse().getWriter().write(json);
        return null;
    }

    @Override
    public User getModel() {
        return user;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
