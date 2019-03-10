package com.cky.controller;

import com.cky.pojo.Users;
import com.cky.pojo.UsersReport;
import com.cky.pojo.vo.PublisherVideo;
import com.cky.pojo.vo.UsersVo;
import com.cky.service.UserService;
import com.cky.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/user")
@Api(value = "用户相关业务的接口",tags = {"用户相关业务的Controller"})
public class UserController extends BaseContrller{
    @Autowired
    private UserService usersService;

    @Value("${com.cky.fileSpace}")
    private String FILE_SPACE;

    @ApiOperation(value = "用户上传头像",notes = "用户上传头像")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId,
                                      @RequestParam("file") MultipartFile[] files) throws Exception {


        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("上传出错了...");
        }
        
        //保存到数据库的相对路径
        String uploadPathDB = File.separator+userId + File.separator+"face";

        if(files != null && files.length > 0){
            FileOutputStream out = null;
            InputStream in = null;
            try {
                String fileName = files[0].getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    String finalFacePath = FILE_SPACE + uploadPathDB + File.separator+fileName;
                    //设置数据库保存的路径
                    uploadPathDB += (File.separator+fileName);
                    File outFile = new File(finalFacePath);
                    //第一种上传文件的方式
//                    if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
//                        //创建父文件夹
//                        outFile.getParentFile().mkdirs();
//                    }
//                    out = new FileOutputStream(finalFacePath);
//                    in = files[0].getInputStream();
//                    IOUtils.copy(in,out);

                    //另外一种上传方式
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdirs();
                    }
                    files[0].transferTo(outFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return IMoocJSONResult.errorMsg("上传出错了...");
            }finally {
                if(out != null){
                    out.flush();
                    out.close();
                }
            }
        }else{
            return IMoocJSONResult.errorMsg("上传出错了...");
        }
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);

        usersService.updateUserInfo(users);
        return IMoocJSONResult.ok(uploadPathDB);

    }


    @ApiOperation(value = "查询用户信息",notes = "查询用户信息接口")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/query")
    public IMoocJSONResult query(String userId,String fanId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("");
        }
        Users userInfo = usersService.queryUserInfo(userId);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(userInfo,usersVo);
        boolean isFollow = usersService.queryIsFollow(userId, fanId);
        usersVo.setFollow(isFollow);

        return IMoocJSONResult.ok(usersVo);
    }


    @PostMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId,String videoId,String publishUserId){
        if(StringUtils.isBlank(publishUserId)){
            return IMoocJSONResult.errorMsg("");
        }
        //1.查询视频发布者的信息
        Users userInfo = usersService.queryUserInfo(publishUserId);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(userInfo,usersVo);

        //2.查询当前登录者和视频点赞关系
        boolean userLikeVideo = usersService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setUsersVo(usersVo);
        publisherVideo.setUserLikeVideo(userLikeVideo);

        return IMoocJSONResult.ok(publisherVideo);
    }


    @PostMapping("/beyourfan")
    public IMoocJSONResult beyourfan(String userId,String fanId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("");
        }
        usersService.saveUserFanRelation(userId,fanId);

        return IMoocJSONResult.ok("关注成功");
    }

    @PostMapping("/dontbeyourfan")
    public IMoocJSONResult dontbeyourfan(String userId,String fanId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("");
        }
        usersService.deleteUserFanRelation(userId,fanId);

        return IMoocJSONResult.ok("取消关注成功");
    }


    @PostMapping("/reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport){

        usersService.reportUser(usersReport);

        return IMoocJSONResult.ok("举报成功");
    }

}
