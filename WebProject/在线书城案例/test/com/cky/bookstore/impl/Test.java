package com.cky.bookstore.impl;

import com.cky.bookstore.dao.BookDao;
import com.cky.bookstore.domian.Book;
import com.cky.bookstore.domian.ShoppingCartItem;
import com.cky.bookstore.web.CriteriaBook;
import com.cky.bookstore.web.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Test {
    private BookDaoImpl dao = new BookDaoImpl();

    @org.junit.Test
    public void insert(){
        Book book = new Book("Ela","MySql",98,new Date(),3,10,"Good50");
        dao.insertDate(book);
    }

    @org.junit.Test
    public void getBook() throws Exception {
        Book book = dao.getBook(2);
        System.out.println(book);
    }

    @org.junit.Test
    public void getPage() throws Exception {

        Page<Book> page = dao.getPage(new CriteriaBook(0, 200, 2));
        System.out.println(page.getPageNo());
        System.out.println(page.getTotalPageNumber());
        System.out.println(page.getList());
        System.out.println(page.getPageSize());
        System.out.println(page.getNextPage());
        System.out.println(page.getPrevPage());
    }

    @org.junit.Test
    public void getTotalBookNumber() throws Exception {
        long totalBookNumber = dao.getTotalBookNumber(new CriteriaBook(0, 200, 2));
        System.out.println(totalBookNumber);
    }

    @org.junit.Test
    public void getPageList() throws Exception {
        List<Book> pageList = dao.getPageList(new CriteriaBook(0, 200, 2), 3);
        System.out.println(pageList);
    }

    @org.junit.Test
    public void getStoreNumber() throws Exception {
        int storeNumber = dao.getStoreNumber(3);
        System.out.println(storeNumber);
    }
    @org.junit.Test
    public void testBentchUpdateStoreNumberAndSalesAmount(){

        Collection<ShoppingCartItem> items = new ArrayList<>();
        Book book = dao.getBook(1);
        ShoppingCartItem sci = new ShoppingCartItem(book);

    }
}
