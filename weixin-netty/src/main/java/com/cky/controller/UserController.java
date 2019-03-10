package com.cky.controller;

import com.cky.enums.OperatorFriendRequestTypeEnum;
import com.cky.enums.SearchFriendsStatusEnum;
import com.cky.pojo.ChatMsg;
import com.cky.pojo.Users;
import com.cky.pojo.bo.UsersBO;
import com.cky.pojo.vo.MyFriendsVO;
import com.cky.pojo.vo.UsersVO;
import com.cky.service.UserService;
import com.cky.utils.FastDFSClient;
import com.cky.utils.FileUtils;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/u")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${com.cky.imgTemSpace}")
    private String IMG_TEM_SPACE;

    @PostMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码不能为空
        if(StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名或密码不能为空...");
        }

        //2.判断用户名是否存在，如果存在就登录，如果不存在就注册
        boolean usernameIsExit = userService.queryUsernameIsExit(user.getUsername());
        Users userResult = null;
        if(usernameIsExit){
            //登录
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if(userResult == null){
                return IMoocJSONResult.errorMsg("用户名或密码不正确...");
            }
        }else{
            //注册
            userResult = userService.saveUser(user);
        }
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult,usersVO);
        return IMoocJSONResult.ok(usersVO);
    }

    /**
     *上传头像
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception {

        //获取前端传过来的base64字符串转换为文件对象在上传
        String base64Data = userBO.getFaceData();
        String userFacePath = IMG_TEM_SPACE + userBO.getUserId()+"userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

        //获取略缩图的url
        String thump = "_80x80.";
        String[] arr = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        //更新用户头像
        Users users = new Users();
        users.setId(userBO.getUserId());
        users.setFaceImage(thumpImgUrl);
        users.setFaceImageBig(url);

        Users result = userService.updateUserInfo(users);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result,usersVO);

        return IMoocJSONResult.ok(usersVO);
    }


    /**
     * 修改昵称
     * @param usersBO
     * @return
     */
    @PostMapping("/setNickname")
    public IMoocJSONResult setNickname(@RequestBody UsersBO usersBO){

        if(StringUtils.isBlank(usersBO.getNickname())){
            return IMoocJSONResult.errorMsg("昵称不能为空...");
        }
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setNickname(usersBO.getNickname());
        Users result = userService.updateUserInfo(user);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result,usersVO);
        return IMoocJSONResult.ok(usersVO);
    }

    /**
     * 搜索朋友接口
     * @param
     * @return
     */
    @PostMapping("/search")
    public IMoocJSONResult searchUser(String myUserId,String friendUsername){

        //判断myUserId 和 friendUsername 不能为空
        if(StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)){
            return IMoocJSONResult.errorMsg("");
        }

        // 前置条件  1.搜索的用户不存在，返回[无此用户]
        // 前置条件  2.搜索的账号是你自己，返回[不能添加自己]
        // 前置条件  3.搜索的账号已经是你的朋友，返回[该用户已经是你的朋友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if(status == SearchFriendsStatusEnum.SUCCESS.status){
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO result = new UsersVO();
            BeanUtils.copyProperties(user,result);
            return IMoocJSONResult.ok(result);
        }else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IMoocJSONResult.errorMsg(errorMsg);
        }


    }


    /**
     * 发送添加朋友的请求
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @PostMapping("/addFriendRequest")
    public IMoocJSONResult addFriendRequest(String myUserId,String friendUsername){

        //判断myUserId 和 friendUsername 不能为空
        if(StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)){
            return IMoocJSONResult.errorMsg("");
        }

        // 前置条件  1.搜索的用户不存在，返回[无此用户]
        // 前置条件  2.搜索的账号是你自己，返回[不能添加自己]
        // 前置条件  3.搜索的账号已经是你的朋友，返回[该用户已经是你的朋友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if(status == SearchFriendsStatusEnum.SUCCESS.status){
            userService.sendFriendRequest(myUserId, friendUsername);
            return IMoocJSONResult.ok();
        }else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IMoocJSONResult.errorMsg(errorMsg);
        }

    }

    /**
     * 查找好友请求列表
     * @param userId
     * @return
     */
    @PostMapping("/queryFriendRequests")
    public IMoocJSONResult queryFriendRequests(String userId){
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("");
        }

        //查询用户接受到的朋友申请
        return IMoocJSONResult.ok(userService.queryFriendRequestList(userId));
    }

    /**
     * 接收方 通过或忽略好友请求
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    @PostMapping("/operFriendRequest")
    public IMoocJSONResult operFriendRequest(String acceptUserId,String sendUserId,
                                             Integer operType){
        //非空校验
        if(StringUtils.isBlank(acceptUserId)||StringUtils.isBlank(sendUserId)||operType == null){
            return IMoocJSONResult.errorMsg("");
        }
        //如果operType 没有对应的枚举值，则抛出空错误信息
        if(StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))){
            return IMoocJSONResult.errorMsg("");
        }
        if(operType == OperatorFriendRequestTypeEnum.IGNORE.type){
            //判断如果忽略好友请求,则直接删除好友请求的数据库记录
            userService.deleteFriendRequest(sendUserId, acceptUserId);
        }else {
            //判断如果是通过好友请求，则互相增加好友记录到数据库对应的表，然后删除好友请求的数据库记录
            userService.passFriendRequest(sendUserId, acceptUserId);
        }
        List<MyFriendsVO> myFriends = userService.queryMyFriends(acceptUserId);
        return IMoocJSONResult.ok(myFriends);
    }



    @PostMapping("/myFriends")
    public IMoocJSONResult myFriends(String userId){

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("");
        }

        //数据库查询好友列表
        List<MyFriendsVO> myFriends = userService.queryMyFriends(userId);
        return IMoocJSONResult.ok(myFriends);
    }

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptUserId
     * @return
     */
    @PostMapping("/getUnReadMsgList")
    public IMoocJSONResult getUnReadMsgList(String acceptUserId){
        if(StringUtils.isBlank(acceptUserId)){
            return IMoocJSONResult.errorMsg("");
        }

        List<ChatMsg> unReadMsgList = userService.getUnReadMsgList(acceptUserId);
        return IMoocJSONResult.ok(unReadMsgList);
    }

}
