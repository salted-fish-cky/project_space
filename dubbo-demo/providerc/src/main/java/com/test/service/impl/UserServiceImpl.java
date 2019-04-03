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
package com.test.service.impl;

import com.demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * The class UserServiceImpl.
 *
 * Description:
 *
 * @author: caokeyu
 * @since: 2019/3/28 19:24
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHi(String name) {
        return "hi 2222"+ name;
    }
}
