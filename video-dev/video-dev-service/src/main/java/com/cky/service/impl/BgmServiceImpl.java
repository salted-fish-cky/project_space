package com.cky.service.impl;

import com.cky.mapper.BgmMapper;
import com.cky.mapper.UsersMapper;
import com.cky.pojo.Bgm;
import com.cky.pojo.Users;
import com.cky.service.BgmService;
import com.cky.service.UserService;
import com.cky.utils.IMoocJSONResult;
import com.cky.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;




    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {

        return bgmMapper.selectAll();
    }

    @Override
    public Bgm queryBgmById(String bgmId) {
        Bgm bgm = bgmMapper.selectByPrimaryKey(bgmId);
        return bgm;
    }
}
