package com.cky.bookstore.impl;

import com.cky.bookstore.dao.DAO;
import com.cky.bookstore.dao.TradeItemdao;
import com.cky.bookstore.domian.TradeItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TradeItemDaoImpl extends DAO<TradeItem> implements TradeItemdao {
    @Override
    public void batchSave(Collection<TradeItem> items) {
        String sql ="insert into tradeitem(bookid,quantity,tradeid) values(?,?,?)";
        ArrayList<TradeItem> tradeItems = new ArrayList<>(items);
        Object[][] params = new Object[tradeItems.size()][3];
        for (int i = 0; i <tradeItems.size() ; i++) {
            params[i][0] = tradeItems.get(i).getBookId();
            params[i][1] = tradeItems.get(i).getQuantity();
            params[i][2] = tradeItems.get(i).getTradeId();

        }

        batch(sql,params);
    }

    @Override
    public Set<TradeItem> getTradeItemWithTradeId(Integer tradeId) {
        String sql = "select * from tradeitem where tradeid=?";

        return new HashSet((getForList(sql,tradeId)));
    }
}
