package com.cky.demo.dao;

import com.cky.demo.bean.CriteriaCustomer;
import com.cky.demo.bean.Customer;
import com.cky.demo.bean.FileUploadBean;

import java.util.List;

public interface CustomerDAO {

    public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc);

    public List<Customer> getAll();

    public void save(Customer customer);

    public Customer get(int id);

    public void update(Customer customer);

    public void delete(int id);


    /**
     * 返回和name对应的条数
     * @param name
     * @return
     */
    public long getCountByName(String name);
}
