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
package com.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class UserContrller.
 *
 * Description:
 *
 * @author: caokeyu
 * @since: 2019/3/29 10:41
 */
@RestController
public class UserController {

    @Reference
    UserService userService;

    @GetMapping("/test/{name}")
    public String test(@PathVariable String name){
        return userService.sayHi(name);
    }
}
