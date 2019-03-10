package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.User;
import com.cky.bos.service.UserService;
import com.cky.bos.utils.BOSUtils;
import com.cky.bos.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

    /**
     *
     */

    private String checkcode;


    @Autowired
    private UserService userService;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }


    /**
     * 用户登录,使用shiro提供的方式进行认证
     * @return
     */
    public String login(){
        //从session中获取生成的验证码
        String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
        //校验验证码是否正确
        if(!StringUtils.isEmpty(checkcode)&&checkcode.equals(key)){

            //使用shiro提供的方式进行认证操作
            Subject subject = SecurityUtils.getSubject();//获取用户状态
            AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), MD5Utils.md5(model.getPassword()));
            try{

                subject.login(token);
            }catch (Exception e){
                e.printStackTrace();
                this.addActionError("用户名或密码错误！");
                return LOGIN;
            }
            User user = (User) subject.getPrincipal();
            ServletActionContext.getRequest().getSession().setAttribute("loginUser",user);
            return HOME;

        }else{
            //输入的验证码错误
            this.addActionError("输入的验证码错误！");
            return LOGIN;
        }
    }



    /**
     * 用户登录
      * @return
     */
 public String login_bac(){
     //从session中获取生成的验证码
     String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
     //校验验证码是否正确
     if(!StringUtils.isEmpty(checkcode)&&checkcode.equals(key)){
        //输入的验证码正确
         User user = userService.login(model);
         if(user != null){
             //登陆成功
             ServletActionContext.getRequest().getSession().setAttribute("loginUser",user);
             return HOME;
         }else{
             this.addActionError("用户名或密码错误！");
             return LOGIN;
         }
     }else{
         //输入的验证码错误
         this.addActionError("输入的验证码错误！");
         return LOGIN;
     }
 }

    /**
     * 修改当前密码
     * @return
     */
 public String editPassword() throws IOException {
     User user = BOSUtils.getLoginUser();
     String f = "1";
     try {
         userService.editPassword(user.getId(),model.getPassword());

     }catch (Exception e){
         e.printStackTrace();
         f = "0";
     }
     ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
     ServletActionContext.getResponse().getWriter().write(f);
     return NONE;
 }

 public String logout(){
     ServletActionContext.getRequest().getSession().invalidate();
     return LOGIN;
 }

 private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String add(){

     userService.save(model,roleIds);
     return LIST;
 }

    /**
     * 用户数据的分页查询
     * @return
     */
 public String pageQuery(){
        userService.pageQuery(pageBean);
        java2Json(pageBean,new String[]{"currentPage","detachedCriteria","pageSize","noticebills","roles","birthday"});
        return NONE;
 }
}
