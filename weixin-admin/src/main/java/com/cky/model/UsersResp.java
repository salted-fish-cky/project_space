/**
 * Copyright (C) 2011-2019 ShenZhen iBoxChain Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary information of iBoxChain Company of China.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the contract agreement you entered into with iBoxChain
 * inc.
 */
package com.cky.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * The class UsersResp.
 *
 * Description:
 *
 * @author: caokeyu
 * @since: 2019/4/1 19:51
 */
@Data
public class UsersResp {

    private String id;

    /**
     * 用户名，账号
     */
    private String username;

    /**
     * 密码
     */
//    private String password;

    /**
     * 我的头像，如果没有默认给一张
     */
    private String faceImage;

    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;

    private String cid;

    /**
     * 0代表离线，1代表在线
     */
    private Integer isOnline = 0;
}
