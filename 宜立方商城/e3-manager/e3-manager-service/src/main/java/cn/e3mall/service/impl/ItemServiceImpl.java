package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;


    @Override
    public TbItem getItemById(long itemId) {
        //添加缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果缓存中没有，则查询数据库
        //根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        //设置查询条件
       criteria.andIdEqualTo(itemId);
       //执行查询
        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
        if(tbItems != null && tbItems.size()>0){
            try {
                jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(tbItems.get(0)));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
                //把结果添加到缓存
                return tbItems.get(0);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item属性
        item.setId(itemId);
        //1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品表中出入数据
        itemDescMapper.insert(itemDesc);
        //发送商品添加消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });
        //返回成功
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(String ids) {
        String[] idStr = ids.split(",");
        for(String id:idStr){
            itemMapper.deleteByPrimaryKey(Long.parseLong(id));
            itemDescMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        return E3Result.ok();
    }

    @Override
    public E3Result instockItem(String ids) {
        String[] idStr = ids.split(",");

        for (String id : idStr){
            TbItem tbItem = itemMapper.selectByPrimaryKey(Long.parseLong(id));
            tbItem.setStatus((byte) 2);
            itemMapper.updateByPrimaryKey(tbItem);
        }
        return E3Result.ok();
    }

    @Override
    public E3Result reshelfItem(String ids) {
        String[] idStr = ids.split(",");

        for (String id : idStr){
            TbItem tbItem = itemMapper.selectByPrimaryKey(Long.parseLong(id));
            tbItem.setStatus((byte) 1);
            itemMapper.updateByPrimaryKey(tbItem);
        }
        return E3Result.ok();
    }

    @Override
    public E3Result queryItemDesc(long id) {
//        TbItemDescExample example = new TbItemDescExample();
//        TbItemDescExample.Criteria criteria = example.createCriteria();
//        criteria.andItemIdEqualTo(id);
//        List<TbItemDesc> itemDescs = itemDescMapper.selectByExample(example);
//        if(itemDescs != null && itemDescs.size()>0){
//            System.out.println(itemDescs.get(0));
//            return E3Result.ok(itemDescs.get(0));
//        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
        if(itemDesc != null){
            return E3Result.ok(itemDesc);
        }
        return null;
    }

    @Override
    public E3Result updateItem(TbItem tbItem, String desc) {
        //先查TbItem
        TbItem item = itemMapper.selectByPrimaryKey(tbItem.getId());
        //补全item的属性
        tbItem.setCreated(item.getCreated());
        tbItem.setStatus((byte) 1);
        tbItem.setUpdated(new Date());

        //更新商品属性
        itemMapper.updateByPrimaryKey(tbItem);

        //先查出对应的商品描述
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(tbItem.getId());
        //更新属性
        itemDesc.setUpdated(new Date());
        itemDesc.setItemDesc(desc);
        //执行更新
        itemDescMapper.updateByPrimaryKey(itemDesc);
        //返回成功
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //添加缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNotBlank(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
            //把结果添加到缓存

        }catch (Exception e){
            e.printStackTrace();
        }

        return itemDesc;

    }


}
