package com.cky.controller;

import com.cky.pojo.AdminUser;
import com.cky.pojo.Users;
import com.cky.service.UserService;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public IMoocJSONResult userLogin(String username, String password, HttpServletRequest request,
                                     HttpServletResponse response){
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
            return IMoocJSONResult.errorMsg("用户名或密码不能为空...");
        }
        if(!username.equals("cky") || !password.equals("1234")){
            return IMoocJSONResult.errorMsg("用户名或密码错误...");
        }
        String usertoken = UUID.randomUUID().toString();
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        adminUser.setUsertoken(usertoken);
        request.getSession().setAttribute("user",adminUser);
        return IMoocJSONResult.ok();
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "login";
    }

    @GetMapping("showList")
    public String showList(){
        return "users/usersList";
    }

    @PostMapping("list")
    @ResponseBody
    public PagedResult list(Users user,Integer page){
        if(page == null){
            page = 1;
        }
        PagedResult pagedResult = userService.queryUserList(user, page, 10);
        return pagedResult;
    }
}
