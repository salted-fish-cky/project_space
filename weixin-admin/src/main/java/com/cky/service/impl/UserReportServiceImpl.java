package com.cky.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.UserReport;
import com.cky.entity.Users;
import com.cky.mapper.UserReportMapper;
import com.cky.mapper.UsersMapper;
import com.cky.model.UserReportResp;
import com.cky.service.IUserReportService;
import com.cky.util.PagedResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UsersMapper usersMapper;
    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {
        Page<UserReport> reportPage = new Page<>(page, pageSize);
        IPage<UserReport> iPage = super.page(reportPage);
        List<UserReport> records = iPage.getRecords();
        List<UserReportResp> userReportRespList = new ArrayList<>();
        if (records != null && records.size() >0) {
            userReportRespList = records.stream().map(ur -> {
                Users user = usersMapper.selectById(ur.getUserId());
                Users dealUser = usersMapper.selectById(ur.getDealUserId());
                UserReportResp userReportResp = new UserReportResp();
                BeanUtils.copyProperties(ur, userReportResp);
                userReportResp.setUsername(user.getUsername());
                userReportResp.setDealUsername(dealUser.getUsername());
                userReportResp.setDealFaceImage(dealUser.getFaceImage());
                userReportResp.setFaceImage(user.getFaceImage());
                return userReportResp;
            }).collect(Collectors.toList());
        }

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal((int) iPage.getTotal());
        pagedResult.setRows(userReportRespList);
        pagedResult.setPage((int) iPage.getCurrent());
        pagedResult.setRecords(iPage.getPages());
        return pagedResult;
    }
}
