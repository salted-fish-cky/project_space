package com.cky.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code){
        log.info("进入auth方法");
        log.info("code={}",code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx23579bb01322caaa&SECRET=c401ff81bad41aedfd849495373a19c9&code=" + code + "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}",response);
    }


    @GetMapping("/token")
    public void test(String signature,String timestamp,String nonce,String echostr, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        // 将token、timestamp、nonce三个参数进行字典序排序
        System.out.println("signature:"+signature);
        System.out.println("timestamp:"+timestamp);
        System.out.println("nonce:"+nonce);
        System.out.println("echostr:"+echostr);
        System.out.println("TOKEN:"+"caokeyu");
        String[] params = new String[] { "caokeyu", timestamp, nonce };
        Arrays.sort(params);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String clearText = params[0] + params[1] + params[2];
        String algorithm = "SHA-1";
        String sign = new String(
                org.apache.commons.codec.binary.Hex.encodeHex(MessageDigest.getInstance(algorithm).digest((clearText).getBytes()), true));
        // 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (signature.equals(sign)) {
            response.getWriter().print(echostr);
        }
    }

}
