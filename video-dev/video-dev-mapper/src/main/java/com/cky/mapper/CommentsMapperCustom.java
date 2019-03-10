package com.cky.mapper;

import com.cky.pojo.vo.CommentsVo;
import com.cky.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<CommentsVo> {

    List<CommentsVo> queryVideoComments(@Param("videoId") String videoId);
}