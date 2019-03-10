package com.cky.bookstore.dao;

import com.cky.bookstore.domian.Trade;

import java.util.Set;

public interface TradeDao {

    public abstract void insert(Trade trade);

    public abstract Set<Trade> getTradeWithUserId(Integer UserId);
}
