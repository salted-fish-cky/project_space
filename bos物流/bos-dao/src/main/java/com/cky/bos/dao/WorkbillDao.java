package com.cky.bos.dao;

import com.cky.bos.domain.Workbill;

import java.util.List;

public interface WorkbillDao extends BaseDao<Workbill>{
    List<Workbill> findNewWorkbills();
}
