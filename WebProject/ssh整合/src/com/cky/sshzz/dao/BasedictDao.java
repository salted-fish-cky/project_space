package com.cky.sshzz.dao;

import com.cky.sshzz.entities.BaseDict;

import java.util.List;

public interface BasedictDao extends BaseDao<BaseDict>{

    List<BaseDict> getListByTypeCode(String dict_type_code);
}
