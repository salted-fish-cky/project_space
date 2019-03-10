package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户注册处理
 *
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    private TbUserMapper userMapper;
    @Override
    public E3Result checkData(String param, int type) {

        //根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1.用户名 2：手机号 3：邮箱
        if(type ==1 ){
            criteria.andUsernameEqualTo(param);
        }else if(type == 2){
            criteria.andPhoneEqualTo(param);
        }else if (type == 3){
            criteria.andEmailEqualTo(param);
        }else{
            return E3Result.build(400,"数据类型错误");
        }
        //执行查询
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        //判断结果中是否包含数据
        //如果有数据返回false
        if(tbUsers != null && tbUsers.size()>0){

            return new E3Result(false);
        }else {
            //如果没有数据返回true
            return new E3Result(true);

        }

    }

    @Override
    public E3Result register(TbUser user) {
        //数据有效性校验
        if(!StringUtils.isNotBlank(user.getUsername()) || !StringUtils.isNotBlank(user.getPassword())
                ||!StringUtils.isNotBlank(user.getPhone())){
            return E3Result.build(400,"用户数据不完整,注册失败");
        }
        if(!(boolean)checkData(user.getUsername(),1).getData()){
            return E3Result.build(400,"此用户名已经被占用");
        }
        if(!(boolean)checkData(user.getPhone(),2).getData()){
            return E3Result.build(400,"此手机号已经被占用");
        }
        //补全pojo的属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //把用户数据插入到数据库
        userMapper.insert(user);
        //返回添加成功
        return E3Result.ok();
    }
}
