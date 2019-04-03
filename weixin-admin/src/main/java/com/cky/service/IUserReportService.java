package com.cky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cky.entity.UserReport;
import com.cky.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
public interface IUserReportService extends IService<UserReport> {

    PagedResult queryReportList(Integer page, Integer pageSize);
}
