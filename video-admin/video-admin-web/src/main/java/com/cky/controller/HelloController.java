package com.cky.controller;

import com.cky.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello");
        return "hello";
    }
    @GetMapping("center")
    public String center(){
        return "center";
    }





}
