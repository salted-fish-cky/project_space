package com.cky.service;

import com.cky.pojo.Bgm;
import com.cky.utils.PagedResult;

public interface VideoService {

    public void addBgm(Bgm bgm);

    public PagedResult queryBgmList(Integer page,Integer pageSize);

    public void deleteBgm(String bgmId);

    PagedResult queryReportList(Integer page, Integer pageSize);

    void updateVideoStatus(String videoId,Integer status);
}
