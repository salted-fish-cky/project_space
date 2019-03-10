package com.cky.spring.jdbc;

import java.util.List;

public class Oder {
    private Integer id;
    private String name;
    private Integer customer_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    @Override
    public String toString() {
        return "Oder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", customer_id=" + customer_id +
                '}';
    }
}
