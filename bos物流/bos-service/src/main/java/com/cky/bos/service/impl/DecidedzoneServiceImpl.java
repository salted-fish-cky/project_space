package com.cky.bos.service.impl;

import com.cky.bos.dao.DecidedzoneDao;
import com.cky.bos.dao.SubAreaDao;
import com.cky.bos.domain.Decidedzone;
import com.cky.bos.domain.Subarea;
import com.cky.bos.service.DecidedzoneService;
import com.cky.bos.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecidedzoneServiceImpl implements DecidedzoneService {
    @Autowired
    private DecidedzoneDao decidedzoneDao;
    @Autowired
    private SubAreaDao subAreaDao;

    /**
     * 添加定区同时关联分区
     * @param subareaid
     * @param model
     */
    public void save(String[] subareaid, Decidedzone model) {
        decidedzoneDao.save(model);

        for(String id:subareaid){
            Subarea subArea = subAreaDao.findById(id);
            subArea.setDecidedzone(model);
        }
    }

    public void pageQuery(PageBean pageBean) {
        decidedzoneDao.pageQuery(pageBean);
    }
}
