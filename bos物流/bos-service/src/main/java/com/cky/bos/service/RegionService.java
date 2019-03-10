package com.cky.bos.service;

import com.cky.bos.domain.Region;
import com.cky.bos.utils.PageBean;

import java.util.List;

public interface RegionService {

    void saveBench(List<Region> regions);

    void pageQuery(PageBean pageBean);

    List<Region> findByQ(String q);

    List<Region> findAll();
}
