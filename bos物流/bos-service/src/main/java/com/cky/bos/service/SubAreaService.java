package com.cky.bos.service;

import com.cky.bos.domain.Subarea;
import com.cky.bos.utils.PageBean;

import java.util.List;

public interface SubAreaService {
    void save(Subarea model);

    void pageQuery(PageBean pageBean);

    List<Subarea> findAll();

    List<Subarea> findListNotAssociation();

    List<Subarea> findListByDecidedzoneId(String decidedzoneId);

    List<Object> findSubAreasGroupByProvince();
}
