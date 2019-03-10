package com.cky.service.impl;

import com.cky.enums.BGMOperatorTypeEnum;
import com.cky.mapper.BgmMapper;
import com.cky.mapper.ReportsMapperCustom;
import com.cky.mapper.VideosMapper;
import com.cky.pojo.Bgm;
import com.cky.pojo.BgmExample;
import com.cky.pojo.Videos;
import com.cky.pojo.vo.Reports;
import com.cky.service.VideoService;
import com.cky.service.utils.ZKCurator;
import com.cky.utils.JsonUtils;
import com.cky.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private ReportsMapperCustom reportsMapperCustom;

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Override
    public void addBgm(Bgm bgm) {
        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.insert(bgm);
        Map<String,String> map = new HashMap<>();
        map.put("operType",BGMOperatorTypeEnum.ADD.type);
        map.put("path",bgm.getPath());
        zkCurator.sendBgmOprator(bgmId,JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryBgmList(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        BgmExample example = new BgmExample();
        List<Bgm> list =  bgmMapper.selectByExample(example);

        PageInfo<Bgm> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }

    @Override
    public void deleteBgm(String bgmId) {
        Bgm bgm = bgmMapper.selectByPrimaryKey(bgmId);
        bgmMapper.deleteByPrimaryKey(bgmId);
        Map<String,String> map = new HashMap<>();
        map.put("operType",BGMOperatorTypeEnum.DELETE.type);
        map.put("path",bgm.getPath());
        zkCurator.sendBgmOprator(bgmId,JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Reports> list = reportsMapperCustom.queryReportList();
        PageInfo<Reports> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }

    @Override
    public void updateVideoStatus(String videoId,Integer status) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setStatus(status);
        videosMapper.updateByPrimaryKeySelective(videos);
    }
}
