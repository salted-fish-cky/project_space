package com.cky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.UserReport;
import com.cky.mapper.UserReportMapper;
import com.cky.service.IUserReportService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@Service
public class UserReportServiceImpl extends ServiceImpl<UserReportMapper, UserReport> implements IUserReportService {

}
