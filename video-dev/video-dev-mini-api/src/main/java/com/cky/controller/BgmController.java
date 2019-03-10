package com.cky.controller;

import com.cky.pojo.Bgm;
import com.cky.service.BgmService;
import com.cky.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "背景音乐业务的接口",tags = {"背景音乐业务的Controller"})
@RestController
@RequestMapping("/bgm")
public class BgmController extends BaseContrller {

    @Autowired
    private BgmService bgmService;


    @ApiOperation(value = "查询背景音乐列表",notes = "查询背景音乐列表接口")
    @PostMapping("/list")
    public IMoocJSONResult queryBgmListBgmController(){
        List<Bgm> bgms = bgmService.queryBgmList();
        return IMoocJSONResult.ok(bgms);
    }
}
