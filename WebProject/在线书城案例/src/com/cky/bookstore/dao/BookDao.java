package com.cky.bookstore.dao;

import com.cky.bookstore.domian.Book;
import com.cky.bookstore.domian.ShoppingCartItem;
import com.cky.bookstore.web.CriteriaBook;
import com.cky.bookstore.web.Page;

import java.util.Collection;
import java.util.List;

public interface BookDao {
    public Book getBook(int id);

    public Page<Book> getPage(CriteriaBook cb);

    public long getTotalBookNumber(CriteriaBook cb);

    public List<Book> getPageList(CriteriaBook cb,int pageSize);

    public int getStoreNumber(Integer id);

    /**
     * 根据传入的ShoppingCartItem集合，批量跟新books数据表的storeNumber和salesnumber字段的值
     * @param items
     */
    public abstract void batchUpdateStoreNumberAndSalesAmount(Collection<ShoppingCartItem> items);
}
