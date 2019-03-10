package com.cky.service;

import com.cky.pojo.Users;
import com.cky.utils.PagedResult;

public interface UserService {

    public PagedResult queryUserList(Users user,Integer page,Integer pageSize);
}
