package com.cky.struts2.token;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 解决表单重复提交问题
 * 在s：form中添加s：token子标签
 *
 * 然后在struts。xml配置token或tokenSession拦截器
 *
 */
public class TokenAction extends ActionSupport{

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String execute() throws Exception {
        Thread.sleep(2000);
        System.out.println(username);
        return SUCCESS;
    }
}
