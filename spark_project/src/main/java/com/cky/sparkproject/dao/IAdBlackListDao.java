package com.cky.sparkproject.dao;

import com.cky.sparkproject.domain.AdBlacklist;

import java.util.List;

/**
 * 广告黑名单接口
 */
public interface IAdBlackListDao {

    void insertBatch(List<AdBlacklist> adBlacklists);

    List<AdBlacklist> findAll();
}
