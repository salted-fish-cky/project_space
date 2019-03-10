package com.cky.bos.service.impl;

import com.cky.bos.dao.RoleDao;
import com.cky.bos.domain.Function;
import com.cky.bos.domain.Role;
import com.cky.bos.service.RoleService;
import com.cky.bos.utils.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    /**
     * 保存一个角色同时还需要关联权限
     * @param model
     * @param functionIds
     */
    public void save(Role model, String functionIds) {
        roleDao.save(model);
        if(StringUtils.isNotBlank(functionIds)){
            String[] split = functionIds.split(",");
            for(String functionId:split){
                Function function = new Function();
                function.setId(functionId);
                //角色关联权限
                model.getFunctions().add(function);
            }
        }
    }

    public void pageQuery(PageBean pageBean) {
        roleDao.pageQuery(pageBean);
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
