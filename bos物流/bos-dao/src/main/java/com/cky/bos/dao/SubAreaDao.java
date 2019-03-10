package com.cky.bos.dao;

import com.cky.bos.domain.Subarea;

import java.util.List;

public interface SubAreaDao extends BaseDao<Subarea>{
    List<Object> findSubAreasGroupByProvince();
}
