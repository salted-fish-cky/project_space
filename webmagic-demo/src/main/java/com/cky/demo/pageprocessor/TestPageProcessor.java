/*
 * Copyright (C) 2011-2019 ShenZhen iBOXCHAIN Information Technology Co.,Ltd.
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

package com.cky.demo.pageprocessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Description：
 *
 * @author caokeyu
 * @since 2019/7/8
 */
public class TestPageProcessor implements PageProcessor {

    public static final String URL_LIST = "https://blog\\.csdn\\.net/\\w+/article/details/\\d+";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    @Override
    public void process(Page page) {
//        page.putField("title", page.getHtml().$("main ul li a","text").all());
//        page.putField("time", page.getHtml().xpath("//main/ul/li//dd[@class='time']/text()").all());
//        page.putField("time", page.getHtml().$("main ul li dd.time","text"));
//        page.putField("read_num", page.getHtml().$("main ul li dd.read_num span.num","text").all());
//        page.putField("common_num", page.getHtml().$("main ul li dd.common_num span.num","text").all());
        page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
        page.putField("title",page.getHtml().$("main div.article-title-box h1","text"));
        page.putField("time",page.getHtml().$("main div.article-info-box span.time","text"));
        page.putField("readNum",page.getHtml().$("main div.article-info-box span.read-count","text"));
        page.putField("content",page.getHtml().$("main article div.article_content"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new TestPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://www.csdn.net/")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
