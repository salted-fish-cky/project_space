package com.cky.service.impl;

import com.cky.mapper.UsersMapper;
import com.cky.pojo.Users;
import com.cky.pojo.UsersExample;
import com.cky.service.UserService;
import com.cky.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public PagedResult queryUserList(Users user, Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        if(StringUtils.isNotBlank(user.getUsername())){
            criteria.andUsernameLike(user.getUsername());
        }
        if(StringUtils.isNotBlank(user.getNickname())){
            criteria.andUsernameLike(user.getNickname());
        }
        List<Users> list = usersMapper.selectByExample(usersExample);
        PageInfo<Users> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }
}
