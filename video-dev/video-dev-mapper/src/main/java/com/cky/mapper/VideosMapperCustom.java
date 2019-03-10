package com.cky.mapper;

import com.cky.pojo.Videos;
import com.cky.pojo.vo.VideoVo;
import com.cky.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideoVo> queryAllVideos(@Param("videoDesc") String videoDesc,@Param("userId") String userId);

    /**
     * 对视频喜欢的数量进行累加
     * @param videoId
     */
    public void addVideoLikeCount(String videoId);


    public List<VideoVo> queryMyLikeVideo(@Param("userId") String userId);

    public List<VideoVo> queryMyFollowVideo(@Param("userId") String userId);

    /**
     * 对视频喜欢的数量进行累减
     * @param videoId
     */
    public void reduceVideoLikeCount(String videoId);

}