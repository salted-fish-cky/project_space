package com.cky.bookstore.impl;

import com.cky.bookstore.dao.BookDao;
import com.cky.bookstore.dao.DAO;
import com.cky.bookstore.domian.Book;
import com.cky.bookstore.domian.ShoppingCartItem;
import com.cky.bookstore.web.CriteriaBook;
import com.cky.bookstore.web.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookDaoImpl extends DAO<Book> implements BookDao {
    @Override
    public Book getBook(int id) {
        String sql = "select * from mybooks where id=?";
        return get(sql,id);
    }

    @Override
    public Page<Book> getPage(CriteriaBook cb) {
        Page page = new Page(cb.getPageNo());
        page.setTotalItemNumber(getTotalBookNumber(cb));
        cb.setPageNo(page.getPageNo());
        page.setList(getPageList(cb,3));
        return page;
    }

    @Override
    public long getTotalBookNumber(CriteriaBook cb) {
        String sql = "select count(id) from mybooks where price>=? and price<=?";
        return getForValue(sql,cb.getMinPrice(),cb.getMaxPrice());
    }

    /**
     * 分页查询
     * @param cb
     * @param pageSize
     * @return
     */
    @Override
    public List<Book> getPageList(CriteriaBook cb, int pageSize) {
        String sql = "select * from mybooks where price>=? and price<=? limit ?,?";
        return getForList(sql,cb.getMinPrice(),cb.getMaxPrice(),(cb.getPageNo()-1)*pageSize,pageSize);
    }



    @Override
    public int getStoreNumber(Integer id) {
        String sql = "select storeNumber from mybooks where id=?";
        return getForValue(sql,id);
    }

    @Override
    public void batchUpdateStoreNumberAndSalesAmount(Collection<ShoppingCartItem> items) {
        String sql = "update mybooks set salesAmount=salesAmount+?," +
                " storeNumber=storeNumber-? where id=?";

        Object[][] params = null;
        params = new Object[items.size()][3];
        List<ShoppingCartItem> scs = new ArrayList<>(items);
        for (int i = 0; i <items.size() ; i++) {
            params[i][0] = scs.get(i).getQuantity();
            params[i][1] = scs.get(i).getQuantity();
            params[i][2] = scs.get(i).getBook().getId();
        }
        batch(sql,params);
    }


    public void insertDate(Book book){
        String sql = "insert into mybooks(author,title,price,publishingDate," +
                "salesAmount,storeNumber,remark) values(?,?,?,?,?,?,?)";

       update(sql,book.getAuthor(),book.getTitle(),book.getPrice(),book.getPublishingDate(),
        book.getSalesAmount(),book.getStoreNumber(),book.getRemark());
    }
}
