package com.cky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cky.entity.Users;
import com.cky.model.UsersResp;
import com.cky.util.PagedResult;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
public interface IUsersService extends IService<Users> {

    PagedResult getUsersList(Users user, Integer page, Integer pageSize);


}
