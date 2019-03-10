package com.cky.demo.impl;

import com.cky.demo.bean.CriteriaCustomer;
import com.cky.demo.bean.Customer;
import com.cky.demo.bean.FileUploadBean;
import com.cky.demo.dao.CustomerDAO;
import com.cky.demo.dao.DAO;

import java.util.List;

public class CustomerDAOJdbcImpl extends DAO<Customer> implements CustomerDAO {

    @Override
    public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc) {
        String sql = "select * from customers where name like ? and " +
                "address like ? and phone like ?";

        return getForList(sql,cc.getName(),cc.getAddress(),cc.getPhone());
    }

    @Override
    public List<Customer> getAll() {
        String sql = "select * from customers";
        return getForList(sql);
    }

    @Override
    public void save(Customer customer) {
        String sql = "insert into customers(name,address,phone) values(?,?,?)";
        update(sql,customer.getName(),customer.getAddress(),customer.getPhone());
    }

    @Override
    public Customer get(int id) {
        String sql = "select * from customers where id=?";
        return get(sql,id);
    }

    @Override
    public void delete(int id) {
        String sql = "delete from customers where id=?";
        update(sql,id);
    }

    @Override
    public long getCountByName(String name) {
        String sql = "select count(id) from customers where name=?";
        return getForValue(sql,name);
    }

    @Override
    public void update(Customer customer) {
        String sql = "update customers set name=?,address=?,phone=? where id=?";
        update(sql,customer.getName(),customer.getAddress(),customer.getPhone(),customer.getId());
    }

}
