package com.cky.service.impl;

import com.cky.mapper.*;
import com.cky.pojo.Comments;
import com.cky.pojo.SearchRecords;
import com.cky.pojo.UsersLikeVideos;
import com.cky.pojo.Videos;
import com.cky.pojo.vo.CommentsVo;
import com.cky.pojo.vo.VideoVo;
import com.cky.service.VideoService;
import com.cky.utils.PagedResult;
import com.cky.utils.TimeAgoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;



    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveVideo(Videos videos) {
        videos.setId(sid.nextShort());
        videosMapper.insertSelective(videos);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page, Integer pageSize) {

        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(sid.nextShort());
            searchRecords.setContent(desc);
            searchRecordsMapper.insert(searchRecords);
        }


        PageHelper.startPage(page, pageSize);
        List<VideoVo> list = videosMapperCustom.queryAllVideos(desc,userId);
        PageInfo<VideoVo> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageInfo.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotWords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
       //1.保存用户和视频的喜欢点赞关联关系表
        String likeId = sid.nextShort();
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setId(likeId);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insert(usersLikeVideos);

        //2.视频喜欢数量累加
        videosMapperCustom.addVideoLikeCount(videoId);
        //3.用户受喜欢数量的累加
        usersMapper.addReceiveLikeCount(videoCreaterId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {


        //1.删除用户和视频的喜欢点赞关联关系表
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);

        //2.视频喜欢数量累减
        videosMapperCustom.reduceVideoLikeCount(videoId);
        //3.用户受喜欢数量的累减
        usersMapper.reduceReceiveLikeCount(videoCreaterId);
    }

    @Override
    public PagedResult getMyLikeVideo(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<VideoVo> list = videosMapperCustom.queryMyLikeVideo(userId);
        PageInfo<VideoVo> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageInfo.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }

    @Override
    public PagedResult getMyFollowVideo(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<VideoVo> list = videosMapperCustom.queryMyFollowVideo(userId);
        PageInfo<VideoVo> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageInfo.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }

    @Override
    public void saveComment(Comments comments) {
        String commentId = sid.nextShort();
        comments.setId(commentId);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PagedResult getVideoComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CommentsVo> list = commentsMapperCustom.queryVideoComments(videoId);
        for (CommentsVo c : list){
            c.setTimeAgoStr(TimeAgoUtils.format(c.getCreateTime()));
        }

        PageInfo<CommentsVo> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageInfo.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }
}
