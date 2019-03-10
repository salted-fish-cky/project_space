package com.cky.controller;

import com.cky.enums.VideoStatusEnum;
import com.cky.pojo.Bgm;
import com.cky.pojo.Comments;
import com.cky.pojo.Videos;
import com.cky.service.BgmService;
import com.cky.service.VideoService;
import com.cky.utils.FetchVideoCover;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.MergeVideoMp3;
import com.cky.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Api(value = "视频相关业务的接口",tags = {"视频相关业务的Controller"})
@RestController
@RequestMapping("/video")
public class VideoController extends BaseContrller {

    @Autowired
    private VideoService videoService;

    @Autowired
    private BgmService bgmService;

    @Value("${com.cky.fileSpace}")
    private String FILE_SPACE;
    @Value("${com.cky.ffmpegEXE}")
    private String FFMPEG_EXE;


    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "视频时长", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })

    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId,
                                  String bgmId, double videoSeconds, int videoHeight, int videoWidth,
                                  String desc,
                                  @ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception {


        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("上传出错了...");
        }


        //保存到数据库的相对路径
        String uploadPathDB = File.separator + userId + File.separator + "video";
        String coverPathDB = File.separator + userId + File.separator +"cover";
        String finalVideoPath = null;

        if (file != null) {
            FileOutputStream out = null;
            InputStream in = null;
            try {
                String fileName = file.getOriginalFilename();

                int endIndex = fileName.lastIndexOf('.');
                System.out.println(endIndex);
                String fileNamePrefix = fileName.substring(0, endIndex);
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                    finalVideoPath = FILE_SPACE + uploadPathDB + File.separator + fileName;
                    //设置数据库保存的路径
                    uploadPathDB += (File.separator + fileName);
                    coverPathDB = coverPathDB + File.separator + fileNamePrefix + ".jpg";
                    File outFile = new File(finalVideoPath);
                    File coverFile = new File(FILE_SPACE + coverPathDB);

                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    if (!coverFile.getParentFile().exists()) {
                        coverFile.getParentFile().mkdirs();
                    }
                    out = new FileOutputStream(finalVideoPath);
                    in = file.getInputStream();
                    IOUtils.copy(in, out);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return IMoocJSONResult.errorMsg("上传出错了...");
            } finally {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            }
        } else {
            return IMoocJSONResult.errorMsg("上传出错了...");
        }

        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.queryBgmById(bgmId);
            MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3(FFMPEG_EXE);
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB = File.separator + userId + File.separator + "video" + File.separator + videoOutputName;
            String videoInputPath = finalVideoPath;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            mergeVideoMp3.convertor(FILE_SPACE + bgm.getPath(), videoInputPath, videoSeconds, finalVideoPath);
        }

        FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG_EXE);
        fetchVideoCover.getCover(finalVideoPath, FILE_SPACE + coverPathDB);
        //保存视频到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoDesc(desc);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());

        videoService.saveVideo(video);
        return IMoocJSONResult.ok();

    }


    /**
     * 分页和搜索查询视频列表
     *
     * @param video
     * @param isSaveRecord 1：需要保存 0:不需要保存
     * @param page
     * @return
     */
    @PostMapping(value = "/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
                                   Integer page, Integer pageSize) {

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedResult pagedResult = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }


    @PostMapping(value = "/hot")
    public IMoocJSONResult hot() {
        return IMoocJSONResult.ok(videoService.getHotWords());
    }

    @PostMapping(value = "/userLike")
    public IMoocJSONResult userLike(String userId, String videoId, String videoCreaterId) {
        videoService.userLikeVideo(userId, videoId, videoCreaterId);
        return IMoocJSONResult.ok();
    }

    @PostMapping(value = "/userUnLike")
    public IMoocJSONResult userUnLike(String userId, String videoId, String videoCreaterId) {
        videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
        return IMoocJSONResult.ok();
    }

    /**
     * 获取用户点赞视频信息
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/showMyLike")
    public IMoocJSONResult showMyLike(String userId, Integer page, Integer pageSize) {

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedResult pagedResult = videoService.getMyLikeVideo(userId, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }


    /**
     * 获取用户关注的用户的视频信息
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/showMyFollow")
    public IMoocJSONResult showMyFollow(String userId, Integer page, Integer pageSize) {

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedResult pagedResult = videoService.getMyFollowVideo(userId, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }


    /**
     * 保存用户留言
     * @param comments
     * @return
     */
    @PostMapping(value = "/saveComment")
    public IMoocJSONResult saveComment(@RequestBody Comments comments,String fatherCommentId,String toUserId) {
        if(StringUtils.isNotBlank(fatherCommentId)&&StringUtils.isNotBlank(toUserId)){
            comments.setFatherCommentId(fatherCommentId);
            comments.setToUserId(toUserId);
        }
        videoService.saveComment(comments);
        return IMoocJSONResult.ok();
    }


    /**
     * 获取视频评论列表
     * @param
     * @return
     */
    @PostMapping(value = "/getVideoComments")
    public IMoocJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) {

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PagedResult pagedResult = videoService.getVideoComments(videoId, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }



}
