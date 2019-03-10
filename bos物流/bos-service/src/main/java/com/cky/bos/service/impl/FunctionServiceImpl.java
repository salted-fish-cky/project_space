package com.cky.bos.service.impl;

import com.cky.bos.dao.FunctionDao;
import com.cky.bos.domain.Function;
import com.cky.bos.domain.User;
import com.cky.bos.service.FunctionService;
import com.cky.bos.utils.BOSUtils;
import com.cky.bos.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionDao functionDao;
    public List<Function> findAll() {
        return functionDao.findAll();
    }

    public void save(Function model) {
        Function parentFunction = model.getParentFunction();
        if(parentFunction != null && parentFunction.getId().equals("")){
            parentFunction.setId(null);
        }
        functionDao.save(model);
    }

    public void pageQuery(PageBean pageBean) {
        functionDao.pageQuery(pageBean);
    }

    public List<Function> findMenu() {
        List<Function> list = null;
        User loginUser = BOSUtils.getLoginUser();
        if(loginUser.getUsername().equals("admin")){
            //超级管理员查询所有菜单
            list = functionDao.findAllMenu();
        }else{
            //其他用户
            list = functionDao.findMenuByUserId(loginUser.getId());
        }
        return list;
    }
}
