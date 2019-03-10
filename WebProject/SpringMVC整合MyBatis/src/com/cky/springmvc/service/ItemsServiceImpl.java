package com.cky.springmvc.service;

import com.cky.springmvc.mapper.ItemsMapper;
import com.cky.springmvc.pojo.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemsServiceImpl  implements ItemsService{

    @Autowired
    private ItemsMapper itemsMapper;
    @Override
    public List<Items> findItemsList() {
        return itemsMapper.selectByExampleWithBLOBs(null);
    }

    @Override
    public Items findItemsById(Integer id) {

        return itemsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Items items) {
        items.setCreatetime(new Date());

        itemsMapper.updateByPrimaryKeyWithBLOBs(items);
    }
}
