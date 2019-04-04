package com.cky.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cky.entity.Users;
import com.cky.mapper.UsersMapper;
import com.cky.model.UsersResp;
import com.cky.service.IUsersService;
import com.cky.util.JsonUtils;
import com.cky.util.PagedResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.print.attribute.standard.PageRanges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Resource
    RestTemplate restTemplate;
    @Value("${isOnline.url}")
    private String ISNOLINE_URL;

    @Override
    public PagedResult getUsersList(Users user, Integer page, Integer pageSize) {
        Page<Users> usersPage = new Page<Users>(page,pageSize);
        String username = user.getUsername();
        String nickname = user.getNickname();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<Users>();
        if (username != null) {
           queryWrapper.eq("username",username);
        }
        if (nickname != null) {
            queryWrapper.eq("nickname",nickname);
        }

        IPage<Users> iPage = super.page(usersPage, queryWrapper);
        List<Users> list = iPage.getRecords();
        List<UsersResp> usersResps = list.stream().map(u -> {
            UsersResp usersResp = new UsersResp();
            BeanUtils.copyProperties(u, usersResp);
            return usersResp;
        }).collect(Collectors.toList());
        Map<String,Object> map = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(ISNOLINE_URL, String.class);
            if (responseEntity.getStatusCodeValue() == 200){
                String body = responseEntity.getBody();
                map = JsonUtils.jsonToPojo(body, Map.class);
            }

            if (map != null && map.size() != 0) {
                for (String key : map.keySet()) {
                    for (UsersResp ur: usersResps) {
                        if (key.equals(ur.getId()) && map.get(key) != null && !"".equals(map.get(key))) {
                            ur.setIsOnline(1);
                            break;
                        }
                    }

                }
            }
        } catch (RestClientException e) {
            log.error("请求失败：{}",e);
        }

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage((int) iPage.getCurrent());
        pagedResult.setRecords(iPage.getPages());
        pagedResult.setRows(usersResps);
        pagedResult.setTotal((int) iPage.getTotal());
        return pagedResult;
    }
}
