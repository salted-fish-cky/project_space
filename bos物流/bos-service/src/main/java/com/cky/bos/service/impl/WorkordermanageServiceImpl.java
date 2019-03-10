package com.cky.bos.service.impl;

import com.cky.bos.dao.WorkordermanageDao;
import com.cky.bos.domain.Workordermanage;
import com.cky.bos.service.WorkordermanageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkordermanageServiceImpl implements WorkordermanageService {
    @Autowired
    private WorkordermanageDao workordermanageDao;
    public void save(Workordermanage model) {
        workordermanageDao.save(model);
    }
}
