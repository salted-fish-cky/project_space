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

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * The class UserReportResp.
 *
 * Description:
 *
 * @author: caokeyu
 * @since: 2019/4/2 15:08
 */
@Data
public class UserReportResp {

    private String id;

    private String dealUsername;

    private String dealFaceImage;

    private String content;

    private String username;

    private String faceImage;

    private String userId;

    private Date createTime;
}
