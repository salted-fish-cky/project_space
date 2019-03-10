package com.cky.spring.hibernate.entities;

public class BookStock {
    private int isbn;
    private int stock;

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookStock bookStock = (BookStock) o;

        if (isbn != bookStock.isbn) return false;
        if (stock != bookStock.stock) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = isbn;
        result = 31 * result + stock;
        return result;
    }
}
