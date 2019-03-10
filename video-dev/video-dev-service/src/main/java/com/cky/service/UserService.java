package com.cky.service;

import com.cky.pojo.Users;
import com.cky.pojo.UsersReport;
import com.cky.utils.IMoocJSONResult;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 保存用户
     * @param user
     */
    public void saveUser(Users user);

    /**
     * 判断登录用户是否存在
     * @param username
     * @param password
     * @return
     */
    public IMoocJSONResult queryUserForLogin(String username,String password);


    /**
     * 用户修改信息
     * @param user
     */
    public void updateUserInfo(Users user);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);


    /**
     * 查询用户是否点赞视频
     * @param userId
     * @param videoId
     * @return
     */
    public boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 增加用户和粉丝的关系
     * @param userId
     * @param fanId
     */
    public void saveUserFanRelation(String userId,String fanId);

    /**
     * 删除用户和粉丝的关系
     * @param userId
     * @param fanId
     */
    public void deleteUserFanRelation(String userId,String fanId);


    /**
     * 查询用户是否关注
     * @param userId
     * @param fanId
     * @return
     */
    public boolean queryIsFollow(String userId,String fanId);

    /**
     * 插入用户举报视频
     * @param usersReport
     */
    void reportUser(UsersReport usersReport);
}
