package cn.e3mall.sso.service;

import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * 用户登录处理
 */
@Service
public class LoginServiceImpl  implements LoginService{

    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIR}")
    private Integer SESSION_EXPIR;
    @Override
    public E3Result userLogin(String username, String password) {
        //1.判断用户和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //2.如果不正确，返回登录失败
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers==null || tbUsers.size() == 0){
            //返回登录失败
            return E3Result.build(400,"用户名或密码错误");
        }
        //取用户信息
        TbUser user = tbUsers.get(0);
        //判断密码是否正确
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            //如果不正确，返回登录失败
            return E3Result.build(400,"用户或密码错误");
        }
        //3.生成token
        String token = UUID.randomUUID().toString();
        //4.吧用户信息写入redis，key：token，value：用户信息
        user.setPassword(null);
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
        //5.是指session的过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIR);
        //6.把token返回

        return E3Result.ok(token);
    }
}
