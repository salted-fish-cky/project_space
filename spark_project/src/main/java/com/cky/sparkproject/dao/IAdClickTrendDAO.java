package com.cky.sparkproject.dao;

import com.cky.sparkproject.domain.AdClickTrend;

import java.util.List;

public interface IAdClickTrendDAO {

    void updateBatch(List<AdClickTrend> adClickTrends);
}
