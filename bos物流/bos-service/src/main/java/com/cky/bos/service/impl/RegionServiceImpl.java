package com.cky.bos.service.impl;

import com.cky.bos.dao.RegionDao;
import com.cky.bos.domain.Region;
import com.cky.bos.service.RegionService;
import com.cky.bos.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;

    /**
     * 区域数据批量保存
     * @param regions
     */
    public void saveBench(List<Region> regions) {
        for(Region region:regions){
            regionDao.saveOrUpdate(region);
        }
    }

    public void pageQuery(PageBean pageBean) {
        regionDao.pageQuery(pageBean);
    }

    public List<Region> findByQ(String q) {
        return regionDao.findByQ(q);
    }

    @Transactional(readOnly = true)
    public List<Region> findAll() {
        return regionDao.findAll();
    }
}
