package com.cky.sshzz.dao;

import com.cky.sshzz.entities.User;

public interface UserDao extends BaseDao<User>{

    User getByUserCode(String userCode);
}
