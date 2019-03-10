package com.cky.controller;

import com.cky.enums.VideoStatusEnum;
import com.cky.pojo.Bgm;
import com.cky.service.VideoService;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Value("${FILE_SPACE}")
    private String FILE_SPACE;

    @GetMapping("/showAddBgm")
    public String showAddBgm(){
        return "video/addBgm";
    }

    @GetMapping("/showBgmList")
    public String showBgmList(){
        return "video/bgmList";
    }

    @PostMapping("/addBgm")
    @ResponseBody
    public IMoocJSONResult addBgm(Bgm bgm){
        videoService.addBgm(bgm);
        return IMoocJSONResult.ok();
    }


    @PostMapping("/delBgm")
    @ResponseBody
    public IMoocJSONResult delBgm(String bgmId){
       videoService.deleteBgm(bgmId);
        return IMoocJSONResult.ok();
    }

    @PostMapping("/queryBgmList")
    @ResponseBody
    public PagedResult queryBgmList(Integer page){
        if(page == null){
            page = 1;
        }
        PagedResult pagedResult = videoService.queryBgmList(page, 5);
        return pagedResult;
    }

    @PostMapping("/bgmUpload")
    @ResponseBody
    public IMoocJSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws Exception {


        //保存到数据库的相对路径
        String uploadPathDB = File.separator + "bgm";

        if(files != null && files.length > 0){
            FileOutputStream out = null;
            InputStream in = null;
            try {
                String fileName = files[0].getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    String finalBmgPath = FILE_SPACE + uploadPathDB + File.separator+fileName;
                    //设置数据库保存的路径
                    uploadPathDB += (File.separator+fileName);
                    File outFile = new File(finalBmgPath);
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

        return IMoocJSONResult.ok(uploadPathDB);

    }

    @GetMapping("/showReportList")
    public String showReportList(){
        return "video/reportList";
    }

    @PostMapping("/reportList")
    @ResponseBody
    public PagedResult reportList(Integer page){
        if(page == null){
            page = 1;
        }
        return videoService.queryReportList(page,10);
    }

    @PostMapping("/forbidVideo")
    @ResponseBody
    public IMoocJSONResult forbidVideo(String videoId){
        videoService.updateVideoStatus(videoId,VideoStatusEnum.FORBID.value);
        return IMoocJSONResult.ok();
    }


}
