package com.cky.bos.service;

import com.cky.bos.domain.Decidedzone;
import com.cky.bos.utils.PageBean;

public interface DecidedzoneService {
    void save(String[] subareaid, Decidedzone model);

    void pageQuery(PageBean pageBean);
}
