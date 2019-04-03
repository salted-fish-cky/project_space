package com.cky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cky.entity.BlackList;
import com.cky.util.IMoocJSONResult;
import com.cky.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
public interface IBlackListService extends IService<BlackList> {

    IMoocJSONResult insertBleakList(String userId,String userReportId);

    PagedResult queryBlackList(Integer page ,Integer pageSize);

    IMoocJSONResult deleteBlackList(String blackListId);

}
