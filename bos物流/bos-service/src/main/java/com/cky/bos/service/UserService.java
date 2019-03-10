package com.cky.bos.service;

import com.cky.bos.domain.User;
import com.cky.bos.utils.PageBean;

public interface UserService {
    User login(User model);

    void editPassword(String id, String password);

    void save(User model, String[] roleIds);

    void pageQuery(PageBean pageBean);
}
