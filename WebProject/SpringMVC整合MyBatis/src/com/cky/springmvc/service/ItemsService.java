package com.cky.springmvc.service;

import com.cky.springmvc.pojo.Items;

import java.util.List;

public interface ItemsService {

    public List<Items> findItemsList();

    public Items findItemsById(Integer id);


    void updateById(Items items);
}
