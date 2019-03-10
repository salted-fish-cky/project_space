package com.cky.service.impl;

import com.cky.mapper.UsersMapper;
import com.cky.pojo.Users;
import com.cky.utils.FastDFSClient;
import com.cky.utils.FileUtils;
import com.cky.utils.QRCodeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Value("${com.cky.imgTemSpace}")
    private String IMG_TEM_SPACE;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private UsersMapper usersMapper;


    @Test
    public void updateUser() throws IOException {
//        String userId = "190115AATMKA1BTC";
//        Users user = new Users();
//        user.setId(userId);
//        String qrCodePath = IMG_TEM_SPACE + userId + "qrcode.png";
//        qrCodeUtils.createQRCode(qrCodePath,"muxin_qrcode:bbb");
//        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
//        String qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
//        user.setQrcode(qrCodeUrl);
//        usersMapper.updateByPrimaryKeySelective(user);
    }


}
