package com.cky.bos.dao;

import com.cky.bos.domain.Function;

import java.util.List;

public interface FunctionDao extends BaseDao<Function>{
    List<Function> findFunctionListByUserId(String id);

    List<Function> findAllMenu();

    List<Function> findMenuByUserId(String id);
}
