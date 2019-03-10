package com.jesper.seckill.controller;

import com.jesper.seckill.result.Result;
import com.jesper.seckill.service.UserService;
import com.jesper.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/regist")
public class RegistController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toRegistPage")
    public String toRegistPage(){
//        System.out.println("regist Page");
        return "regist";
    }

    @RequestMapping("/do_regist")
    @ResponseBody
    public Result regist(LoginVo loginVo){
        Result result = userService.regist(loginVo);
        return result;
    }
}
