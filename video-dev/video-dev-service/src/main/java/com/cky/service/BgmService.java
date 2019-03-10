package com.cky.service;

import com.cky.pojo.Bgm;

import java.util.List;

public interface BgmService {
    /**
     * 查询背景音乐列表
     * @return
     */
    public List<Bgm> queryBgmList();

    /**
     * 查询背景音乐信息
     * @param bgmId
     * @return
     */
    public Bgm queryBgmById(String bgmId);
}
