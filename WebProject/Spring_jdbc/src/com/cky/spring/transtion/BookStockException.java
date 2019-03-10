package com.cky.spring.transtion;

public class BookStockException extends RuntimeException {

    public BookStockException() {
    }

    public BookStockException(String s) {
        super(s);
    }

    public BookStockException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BookStockException(Throwable throwable) {
        super(throwable);
    }

    public BookStockException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
