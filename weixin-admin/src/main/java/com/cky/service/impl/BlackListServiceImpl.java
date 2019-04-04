package com.cky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.BlackList;
import com.cky.entity.Users;
import com.cky.mapper.BlackListMapper;
import com.cky.mapper.UserReportMapper;
import com.cky.mapper.UsersMapper;
import com.cky.model.BlackListResp;
import com.cky.service.IBlackListService;
import com.cky.util.IMoocJSONResult;
import com.cky.util.PagedResult;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.print.attribute.standard.PageRanges;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@Slf4j
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements IBlackListService {

    @Autowired
    private Sid sid;

    @Autowired
    UserReportMapper userReportMapper;

    @Autowired
    UsersMapper usersMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public IMoocJSONResult insertBleakList(String userId, String userReportId) {
        String blackListId = sid.nextShort();
        BlackList blackList = new BlackList();
        blackList.setId(blackListId);
        blackList.setUserId(userId);
        blackList.setCreateTime(new Date());
        boolean isOk = super.save(blackList);
        if (isOk) {
            userReportMapper.deleteById(userReportId);
            return IMoocJSONResult.ok();
        }
        return IMoocJSONResult.errorMsg("拉黑失败");

    }

    @Override
    public PagedResult queryBlackList(Integer page, Integer pageSize) {
        Page<BlackList> blackListPage = new Page<>(page, pageSize);
        IPage<BlackList> iPage = super.page(blackListPage);
        List<BlackList> records = iPage.getRecords();
        List<BlackListResp> blackListResps = records.stream().map(bl -> {
            Users user = usersMapper.selectById(bl.getUserId());
            BlackListResp blackListResp = new BlackListResp();
            log.info("createDate:{}"+bl.getCreateTime());
            BeanUtils.copyProperties(bl, blackListResp);
            blackListResp.setUsername(user.getUsername());
            blackListResp.setFaceImage(user.getFaceImage());

            return blackListResp;
        }).collect(Collectors.toList());

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage((int) iPage.getCurrent());
        pagedResult.setRecords(iPage.getPages());
        pagedResult.setTotal((int) iPage.getTotal());
        pagedResult.setRows(blackListResps);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public IMoocJSONResult deleteBlackList(String blackListId) {
        boolean isOk = super.removeById(blackListId);
        if (isOk) {
            return IMoocJSONResult.ok();
        } else {
            return IMoocJSONResult.errorMsg("取消拉黑失败");
        }
    }
}
