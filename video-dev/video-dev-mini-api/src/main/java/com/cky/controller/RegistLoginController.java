package com.cky.controller;

import com.cky.pojo.Users;
import com.cky.pojo.vo.UsersVo;
import com.cky.service.UserService;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录的接口",tags = {"注册和登录的Controller"})
public class RegistLoginController extends BaseContrller{
    @Autowired
    private UserService usersService;

    @ApiOperation(value = "用户注册",notes = "用户注册的接口")
    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码不为空
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
        }
        //2.判断用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(user.getUsername());
        if(isExist){
            return IMoocJSONResult.errorMsg("用户名已经存在，请换个试试！");
        }else{
            //3.保存用户，注册信息
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
           user.setFollowCounts(0);
            user.setReceiveLikeCounts(0);

            usersService.saveUser(user);

        }
        user.setPassword("");
        UsersVo usersVo = setUserRedisSessionToken(user);
        return IMoocJSONResult.ok(usersVo);

    }
    @ApiOperation(value = "用户登录",notes = "用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user){
        //1.判断用户名和密码不为空
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
        }
        IMoocJSONResult iMoocJSONResult = usersService.queryUserForLogin(user.getUsername(), user.getPassword());
        if(iMoocJSONResult.isOK()){
            Users result = (Users) iMoocJSONResult.getData();
            UsersVo usersVo = setUserRedisSessionToken(result);
            iMoocJSONResult.setData(usersVo);
        }
        return iMoocJSONResult;
    }

    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
    dataType = "String",paramType = "query")
    @RequestMapping ("/logout")
    public IMoocJSONResult logout(String userId){

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空！");
        }
        redisOperator.del(USER_REDIS_SESSION+":"+userId);
        return IMoocJSONResult.ok();

    }


    public UsersVo setUserRedisSessionToken(Users userModel){
        String token = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION+":"+userModel.getId(),token,1000*60*30);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(userModel,usersVo);
        usersVo.setUserToken(token);
        return usersVo;
    }
}
