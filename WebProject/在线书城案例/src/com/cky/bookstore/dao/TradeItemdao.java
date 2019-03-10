package com.cky.bookstore.dao;

import com.cky.bookstore.domian.TradeItem;

import java.util.Collection;
import java.util.Set;

public interface TradeItemdao {

    /**
     *  批量保存tradeItem对象
     * @param items
     */
    public abstract void batchSave(Collection<TradeItem> items);

    /**
     * 根据tradeId获取和其相关联的TradeItem集合
     * @param tradeId
     * @return
     */
    public abstract Set<TradeItem> getTradeItemWithTradeId(Integer tradeId);

}
