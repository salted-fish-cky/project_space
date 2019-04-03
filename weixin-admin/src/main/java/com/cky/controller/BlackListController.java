package com.cky.controller;


import com.cky.service.IBlackListService;
import com.cky.util.IMoocJSONResult;
import com.cky.util.PagedResult;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@Controller
@RequestMapping("/blackList")
public class BlackListController {

    @Autowired
    IBlackListService blackListService;
    @PostMapping("/pullBlack")
    public IMoocJSONResult pullBlack(@RequestParam String userId,String userReportId){
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(userReportId)){
            return IMoocJSONResult.errorMsg("拉黑失败");
        }
        return blackListService.insertBleakList(userId,userReportId);
    }

    @GetMapping("/showBlackList")
    public String showBlackList(){
        return "users/blackList";
    }

    @PostMapping("/getBlackList")
    @ResponseBody
    public PagedResult getBlackList(Integer page ,Integer pageSize) {
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }
        return blackListService.queryBlackList(page, pageSize);

    }

    @PostMapping("/cancelPullBlack")
    @ResponseBody
    public IMoocJSONResult cancelPullBlack(String blackListId){
        if(StringUtils.isBlank(blackListId)) {
            IMoocJSONResult.errorMsg("取消拉黑失败");
        }
        return blackListService.deleteBlackList(blackListId);
    }

}

