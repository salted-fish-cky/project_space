package com.cky.bos.service;

import com.cky.bos.domain.Function;
import com.cky.bos.utils.PageBean;

import java.util.List;

public interface FunctionService {
    List<Function> findAll();

    void save(Function model);

    void pageQuery(PageBean pageBean);

    List<Function> findMenu();
}
