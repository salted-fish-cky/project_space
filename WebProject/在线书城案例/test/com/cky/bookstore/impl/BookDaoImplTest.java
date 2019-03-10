package com.cky.bookstore.impl;

import com.cky.bookstore.dao.BookDao;
import com.cky.bookstore.domian.Book;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;


@RunWith(Arquillian.class)
public class BookDaoImplTest {

    private BookDaoImpl dao = new BookDaoImpl();

    @Test
    public void insert(){
        Book book = new Book("tom","java",70,new Date(),4,1,"Good10");
        dao.insertDate(book);
    }



    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookDaoImpl.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
