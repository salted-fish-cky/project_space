package com.cky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cky.entity.Users;
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

    List<Users> getUsersList();

}
