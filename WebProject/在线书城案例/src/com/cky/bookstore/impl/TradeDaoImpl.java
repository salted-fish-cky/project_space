package com.cky.bookstore.impl;

import com.cky.bookstore.dao.DAO;
import com.cky.bookstore.dao.TradeDao;
import com.cky.bookstore.domian.Trade;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TradeDaoImpl extends DAO<Trade> implements TradeDao{
    @Override
    public void insert(Trade trade) {
        String sql = "insert into trade (userid,tradetime) values(?,?)";
        int id = (int) insert(sql,trade.getUserId(),trade.getTradeTime());
        trade.setTradeId(id);
    }

    @Override
    public Set<Trade> getTradeWithUserId(Integer UserId) {
        String sql = "select * from trade  where userid=? order by tradetime desc";
        return new LinkedHashSet(getForList(sql,UserId));
    }
}
