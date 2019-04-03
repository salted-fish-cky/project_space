/*
 * Copyright (C) 2011-2018 ShenZhen iBOXCHAIN Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxChain Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxChain inc.
 *
 */
package com.cky.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The class MyBatisPlusConfig.
 *
 * Description:
 *
 * @author: yiyuanke
 * @since: 2018/12/26 16:39
 */
@Configuration
@MapperScan("com.cky.mapper")
public class MyBatisPlusConfig {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 性能分析拦截器，不建议生产使用
     * @return
     */
//    @Bean
//    @Profile( {"kaifa","ceshi"} )
//    public PerformanceInterceptor performanceInterceptor(){
//        return new PerformanceInterceptor();
//    }

}
