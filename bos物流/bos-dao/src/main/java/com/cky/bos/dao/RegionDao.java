package com.cky.bos.dao;

import com.cky.bos.domain.Region;

import java.util.List;

public interface RegionDao extends BaseDao<Region>{
    List<Region> findByQ(String q);
}
