package com.cky.demo.impl;

import com.cky.demo.bean.CriteriaCustomer;
import com.cky.demo.bean.Customer;
import com.cky.demo.bean.FileUploadBean;
import com.cky.demo.dao.CustomerDAO;
import org.junit.Test;

import java.util.List;


public class CustomerDAOJdbcImplTest {
    private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();
    @Test
    public void getAll() throws Exception {
        List<Customer> customers = customerDAO.getAll();
        System.out.println(customers.get(0).getName());
    }

    @Test
    public void save() throws Exception {
        Customer customer = new Customer("43fer","东京","155654354" +
                "32655");
        customerDAO.save(customer);
    }

    @Test
    public void get() throws Exception {
        Customer customer = customerDAO.get(1);
        System.out.println(customer);
    }

    @Test
    public void delete() throws Exception {
        customerDAO.delete(2);
    }

    @Test
    public void getCountByName() throws Exception {
        long cc = customerDAO.getCountByName("cc");
        System.out.println(cc);
    }

    @Test
    public void testgetForListWithCriteriaCustomer(){
        CriteriaCustomer cc = new CriteriaCustomer("k", null, "1");
        List<Customer> ccs = customerDAO.getForListWithCriteriaCustomer(cc);
        System.out.println(ccs.get(0).getName());
    }

    @org.junit.Test
    public void test() throws Exception {
        List<Customer> beans = customerDAO.getAll();
        for (Customer bean:beans){
            System.out.println(bean.getId()+"-"+bean.getName()
                    +"-"+bean.getAddress()+"-"+bean.getPhone());
        }
    }
}
