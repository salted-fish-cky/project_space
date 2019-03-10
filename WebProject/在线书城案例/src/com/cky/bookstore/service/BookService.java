package com.cky.bookstore.service;

import com.cky.bookstore.dao.*;
import com.cky.bookstore.domian.*;
import com.cky.bookstore.impl.*;
import com.cky.bookstore.web.CriteriaBook;
import com.cky.bookstore.web.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class BookService {
    private BookDao bookDao = new BookDaoImpl();
    private AccountDao accountDao = new AccountDaoImpl();
    private TradeItemdao tradeItemdao = new TradeItemDaoImpl();
    private UserDao userDao = new UserDaoImpl();
    private TradeDao tradeDao = new TradeDaoImpl();

    public Page<Book> getPage(CriteriaBook book){
        return bookDao.getPage(book);

    }

    public Book getBook(int id){
        return bookDao.getBook(id);
    }

    public boolean addToCart(int id,ShoppingCart sc) {
        Book book = bookDao.getBook(id);
        if(book!=null){
            sc.addBook(book);
            return true;
        }else{
            return false;
        }

    }

    public void removeItemFromShoppingCart(ShoppingCart sc, int id) {
        sc.removeItem(id);
    }

    public void clearShoppingCart(ShoppingCart sc) {
        sc.clear();
    }

    public void updateItemQuantity(ShoppingCart sc, int id, int quantity) {
        sc.updateItemQuantity(id,quantity);
    }

    public void cash(ShoppingCart shoppingCart, String username, String accountId) {
        //1.更新mybooks 数据表相关记录的salesamount和storenumber
        bookDao.batchUpdateStoreNumberAndSalesAmount(shoppingCart.getItems());
        //2.更新account数据表的balance
        accountDao.updateBalance(Integer.parseInt(accountId),shoppingCart.getTotalMoney());
        //3.向trade数据表插入一条记录
        Trade trade = new Trade();
        trade.setTradeTime(new Date(new Date().getTime()));
        trade.setUserId(userDao.getUser(username).getUserId());
        tradeDao.insert(trade);
        //4.向tradeItem数据表插入n条数据
        Collection<TradeItem> items = new ArrayList<>();
        for(ShoppingCartItem item:shoppingCart.getItems()){
            TradeItem tradeItem = new TradeItem();
            tradeItem.setBookId(item.getBook().getId());
            tradeItem.setQuantity(item.getQuantity());
            tradeItem.setTradeId(trade.getTradeId());

            items.add(tradeItem);
        }
        tradeItemdao.batchSave(items);

        //5.清空购物车
        shoppingCart.clear();
    }
}
