package com.cky.controller;


import com.cky.service.IUserReportService;
import com.cky.util.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/userReport")
public class UserReportController {

    @Autowired
    IUserReportService userReportService;

    @GetMapping("/showReportList")
    public String showReport(){
        return "users/reportList";
    }

    @PostMapping("/reportList")
    @ResponseBody
    public PagedResult getReportList(Integer page,Integer pageSize){
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        return userReportService.queryReportList(page,pageSize);

    }

}

