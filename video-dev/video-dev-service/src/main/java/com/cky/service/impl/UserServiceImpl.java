package com.cky.service.impl;

import com.cky.mapper.UsersFansMapper;
import com.cky.mapper.UsersLikeVideosMapper;
import com.cky.mapper.UsersMapper;
import com.cky.mapper.UsersReportMapper;
import com.cky.pojo.Users;
import com.cky.pojo.UsersFans;
import com.cky.pojo.UsersLikeVideos;
import com.cky.pojo.UsersReport;
import com.cky.service.UserService;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS) //查询用supports
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users user = usersMapper.selectOne(users);
        return user == null ? false:true;
    }

    @Transactional(propagation = Propagation.REQUIRED)//增删改用required
    @Override
    public void saveUser(Users user) {
        //随机生成一个id
        user.setId(sid.nextShort());
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IMoocJSONResult queryUserForLogin(String username,String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        Users result = usersMapper.selectOneByExample(example);
        if(result == null){
            return IMoocJSONResult.errorMsg("用户名错误！");
        }else {
            try {
                if(!MD5Utils.getMD5Str(password).equals(result.getPassword())){
                    return IMoocJSONResult.errorMsg("密码错误！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        result.setPassword("");
        return IMoocJSONResult.ok(result);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",user.getId());
        usersMapper.updateByExampleSelective(user,example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",userId);
        Users user = usersMapper.selectOneByExample(example);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return false;
        }
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);

        if(list != null && list.size() > 0){
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {

        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        usersFansMapper.deleteByExample(example);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollersCount(fanId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryIsFollow(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);

        List<UsersFans> list = usersFansMapper.selectByExample(example);

        if(list != null && list.size() > 0){
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport usersReport) {
        String reportId = sid.nextShort();
        usersReport.setId(reportId);
        usersReport.setCreateDate(new Date());
        usersReportMapper.insert(usersReport);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {

        String relId = sid.nextShort();
        UsersFans usersFans = new UsersFans();
        usersFans.setId(relId);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        usersFansMapper.insert(usersFans);

        usersMapper.addFansCount(userId);
        usersMapper.addFollersCount(fanId);
    }
}
