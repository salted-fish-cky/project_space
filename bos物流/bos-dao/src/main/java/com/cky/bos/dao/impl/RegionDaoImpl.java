package com.cky.bos.dao.impl;

import com.cky.bos.dao.RegionDao;
import com.cky.bos.domain.Region;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionDaoImpl extends BaseDaoImpl<Region> implements RegionDao {
    public List<Region> findByQ(String q) {
        String hql = "from Region r where r.citycode like ? or r.shortcode like ?" +
                " or r.province like ? or r.city like ? or r.district like ?";
        List<Region> regions = (List<Region>) this.getHibernateTemplate().find(hql, "%"+q+"%", "%"+q+"%", "%"+q+"%", "%"+q+"%", "%"+q+"%");
        return regions;
    }
}
