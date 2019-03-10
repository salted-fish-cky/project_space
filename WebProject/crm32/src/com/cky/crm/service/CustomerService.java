package com.cky.crm.service;

import com.cky.crm.beans.Customer;

import javax.jws.WebService;
import java.io.Serializable;
import java.util.List;

@WebService
public interface CustomerService {

    public List<Customer> findAll();

    //查询未关联到定区的客户
    public List<Customer> findListNotAssociation();

    //查询已关联到指定定区的客户
    public List<Customer> findListHasAssociation(Serializable decidedzone_id);

    //定区关联客户
    public void assigncustomerstodecidedzone(String decidedzoneId,Integer[] customerIds);
    //根据客户手机号查客户信息
    public Customer findCustomerByTelephone(String telephone);
    //根据客户地址查询定区id
    public String findDecidedzoneIdByAddress(String address);
}
