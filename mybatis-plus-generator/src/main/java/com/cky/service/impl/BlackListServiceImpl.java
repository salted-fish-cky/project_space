package com.cky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.BlackList;
import com.cky.mapper.BlackListMapper;
import com.cky.service.IBlackListService;
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
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements IBlackListService {

}
