package cn.e3mall.content.service;

import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        try{
            //缓存同步
            jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return E3Result.ok();
    }

    /**
     * 根据内容分类id查询内容分类列表
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {

        //查询缓存
        try {
            //如果缓存中有直接响应结果
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if(StringUtils.isNotBlank(json)){
                JsonUtils.jsonToList(json,TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //如果没有则查数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> contents = contentMapper.selectByExampleWithBLOBs(example);
        //把结果添加到缓存
        try {
            jedisClient.hset(CONTENT_LIST,cid+"", JsonUtils.objectToJson(contents));
        }catch (Exception e){
            e.printStackTrace();
        }
        return contents;
    }
}
