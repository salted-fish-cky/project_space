package com.cky.sparkproject.dao;

import com.cky.sparkproject.domain.AdStat;

import java.util.List;

public interface IAdStatDAO {

    void updateBatch(List<AdStat> adStats);
}
