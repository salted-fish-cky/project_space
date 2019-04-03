package com.cky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.Users;
import com.cky.mapper.UsersMapper;
import com.cky.service.IUsersService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Override
    public List<Users> getUsersList() {
        return super.list(new QueryWrapper<>());
    }
}
