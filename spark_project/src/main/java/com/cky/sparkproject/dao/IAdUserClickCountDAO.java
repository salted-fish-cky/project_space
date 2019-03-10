package com.cky.sparkproject.dao;

import com.cky.sparkproject.domain.AdUserClickCount;

import java.util.List;

public interface IAdUserClickCountDAO {

    void updateBatch(List<AdUserClickCount> list);

    /**
     * 查询用户点击量
     * @param date
     * @param userId
     * @param adId
     * @return
     */
    int findByMultiKey(String date,long userId,long adId);
}
