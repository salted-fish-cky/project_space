package com.cky.sshzz.service.impl;

import com.cky.sshzz.dao.BasedictDao;
import com.cky.sshzz.entities.BaseDict;
import com.cky.sshzz.service.BaseDictService;

import java.util.List;

public class BaseDictServiceImpl implements BaseDictService{
    private BasedictDao basedictDao;

    public void setBasedictDao(BasedictDao basedictDao) {
        this.basedictDao = basedictDao;
    }

    @Override
    public List<BaseDict> getListByTypeCode(String dict_type_code) {
        return basedictDao.getListByTypeCode(dict_type_code);
    }
}
