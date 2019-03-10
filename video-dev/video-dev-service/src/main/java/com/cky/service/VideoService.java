package com.cky.service;

import com.cky.pojo.Comments;
import com.cky.pojo.Videos;
import com.cky.utils.PagedResult;

import java.util.List;

public interface VideoService {


    /**
     * 保存视频
     * @param videos
     */
    public void saveVideo(Videos videos);


    /**
     * 分页查询视频列表
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page,Integer pageSize);


    /**
     * 获取热搜列表
     * @return
     */
    public List<String> getHotWords();


    /**
     * 用户点赞视频
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userLikeVideo(String userId,String videoId,String videoCreaterId);


    /**
     * 用户取消点赞视频
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userUnLikeVideo(String userId,String videoId,String videoCreaterId);


    /**
     * 获取用户点赞视频列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult getMyLikeVideo(String userId,Integer page,Integer pageSize);

    /**
     * 获取用户关注的用户的视频列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult getMyFollowVideo(String userId,Integer page,Integer pageSize);

    /**
     * 保存视频留言
     * @param comments
     */
    void saveComment(Comments comments);

    /**
     * 获取视频评论列表
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getVideoComments(String videoId, Integer page, Integer pageSize);
}
