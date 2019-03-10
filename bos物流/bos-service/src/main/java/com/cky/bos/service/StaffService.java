package com.cky.bos.service;

import com.cky.bos.domain.Staff;
import com.cky.bos.utils.PageBean;

import java.util.List;

public interface StaffService {
    void add(Staff model);

    void pageQuery(PageBean pageBean);

    List<Staff> findListNotDelete();

    void deleteBatch(List<String> ids);
}
