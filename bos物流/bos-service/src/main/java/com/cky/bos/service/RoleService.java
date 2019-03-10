package com.cky.bos.service;

import com.cky.bos.domain.Role;
import com.cky.bos.utils.PageBean;

import java.util.List;

public interface RoleService {
    void save(Role model, String functionIds);

    void pageQuery(PageBean pageBean);

    List<Role> findAll();
}
