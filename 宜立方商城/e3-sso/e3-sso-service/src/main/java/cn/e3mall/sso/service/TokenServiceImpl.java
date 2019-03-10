package cn.e3mall.sso.service;

import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 根据token取用户信息
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIR}")
    private Integer SESSION_EXPIR;
    @Override
    public E3Result getUserByToken(String token) {

        //根据token到redis中取用户信息
        String json = jedisClient.get("SESSION:"+token);
        if(StringUtils.isBlank(json)){
            //娶不到用户信息，登录已经过期，返回登录过期
            return E3Result.build(201,"用户登录已经过期");
        }
        //取到用户信息更新token的过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIR);
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);

        return E3Result.ok(user);
    }
}
