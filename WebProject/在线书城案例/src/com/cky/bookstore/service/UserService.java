package com.cky.bookstore.service;

import com.cky.bookstore.dao.BookDao;
import com.cky.bookstore.dao.TradeDao;
import com.cky.bookstore.dao.TradeItemdao;
import com.cky.bookstore.dao.UserDao;
import com.cky.bookstore.domian.Trade;
import com.cky.bookstore.domian.TradeItem;
import com.cky.bookstore.domian.User;
import com.cky.bookstore.impl.BookDaoImpl;
import com.cky.bookstore.impl.TradeDaoImpl;
import com.cky.bookstore.impl.TradeItemDaoImpl;
import com.cky.bookstore.impl.UserDaoImpl;

import java.util.Set;

public class UserService {

    private UserDao userDao = new UserDaoImpl();
    private TradeDao tradeDao = new TradeDaoImpl();
    private TradeItemdao tradeItemdao = new TradeItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();

    public User getUserByUserName(String name){
        return userDao.getUser(name);
    }


    public User getUserWithTrades(String username){
        User user = userDao.getUser(username);
        if(user== null){
            return null;
        }
        Integer userId = user.getUserId();
        //调用TradeItemDao的方法获取每一个trade中的TradeItem的集合，并把其装配为trade的属性
        Set<Trade> trades = tradeDao.getTradeWithUserId(userId);
        if(trades!=null){
            for(Trade trade:trades){
                Integer tradeId = trade.getTradeId();
                Set<TradeItem> items = tradeItemdao.getTradeItemWithTradeId(tradeId);
                if(items!=null){
                    for(TradeItem item:items){
                        item.setBook(bookDao.getBook(item.getBookId()));
                    }
                }
                trade.setItems(items);
            }
            user.setTrades(trades);

        }
        return user;
    }
}
