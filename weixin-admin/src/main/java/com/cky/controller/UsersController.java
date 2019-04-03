package com.cky.controller;


import com.cky.entity.Users;
import com.cky.model.AdminUser;
import com.cky.service.IUsersService;
import com.cky.util.IMoocJSONResult;
import com.cky.util.PagedResult;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@RequestMapping("/users")
@Controller
public class UsersController {

    @Autowired
    IUsersService usersService;
    @PostMapping("/list")
    @ResponseBody
    public PagedResult getUsers(Users user, Integer page, Integer pageSize){
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        PagedResult pagedResult = usersService.getUsersList(user, page, pageSize);
        return pagedResult;
    }

    @GetMapping("/showList")
    public String showList(){
        return "users/usersList";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public IMoocJSONResult userLogin(String username, String password, HttpServletRequest request) {
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
            return IMoocJSONResult.errorMsg("用户名或密码不能为空...");
        }
        if(!"admin".equals(username) || !"1234".equals(password)){
            return IMoocJSONResult.errorMsg("用户名或密码错误...");
        }

        String usertoken = UUID.randomUUID().toString();
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        adminUser.setUserToken(usertoken);
        request.getSession().setAttribute("user",adminUser);
        return IMoocJSONResult.ok();
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "login";
    }



}

