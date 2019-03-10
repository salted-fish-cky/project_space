package com.cky.crm.service;

import com.cky.crm.beans.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional
public class CustomerServiceImpl implements CustomerService {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        String sql = "select * from t_customer";
        List<Customer> customers = jdbcTemplate.query(sql, new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String station = resultSet.getString("station");
                String telephone = resultSet.getString("telephone");
                String address = resultSet.getString("address");
                String decidedzone_id = resultSet.getString("decidedzone_id");
                return new Customer(id, name, station , telephone, address, decidedzone_id);
            }
        });
        return customers;
    }

    @Override
    public List<Customer> findListNotAssociation() {
        String sql = "select * from t_customer where decidedzone_id is null";
        List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String station = resultSet.getString("station");
                String telephone = resultSet.getString("telephone");
                String address = resultSet.getString("address");
                String decidedzone_id = resultSet.getString("decidedzone_id");
                return new Customer(id, name, station , telephone, address, decidedzone_id);
            }
        });
        return list;
    }

    @Override
    public List<Customer> findListHasAssociation(Serializable decidedzone_id) {

        String sql = "select * from t_customer where decidedzone_id=?";
        List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String station = resultSet.getString("station");
                String telephone = resultSet.getString("telephone");
                String address = resultSet.getString("address");
                String decidedzone_id = resultSet.getString("decidedzone_id");
                return new Customer(id, name, station , telephone, address, decidedzone_id);
            }
        },decidedzone_id);
        return list;
    }

    @Override
    public void assigncustomerstodecidedzone(String decidedzoneId, Integer[] customerIds) {
        String sql = "update t_customer set decidedzone_id = null where decidedzone_id = ?";
        jdbcTemplate.update(sql,decidedzoneId);
        sql = "update t_customer set decidedzone_id = ? where id = ?";
        for(Integer id:customerIds){
            jdbcTemplate.update(sql,decidedzoneId,id);
        }
    }

    @Override
    public Customer findCustomerByTelephone(String telephone) {
        String sql = "select * from t_customer where telephone=?";
        List<Customer> list = jdbcTemplate.query(sql, new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String station = resultSet.getString("station");
                String telephone = resultSet.getString("telephone");
                String address = resultSet.getString("address");
                String decidedzone_id = resultSet.getString("decidedzone_id");
                return new Customer(id, name, station , telephone, address, decidedzone_id);
            }
        },telephone);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String findDecidedzoneIdByAddress(String address) {
        String sql = "select decidedzone_id from t_customer where address=?";
        String decidedzoneId = jdbcTemplate.queryForObject(sql, String.class, address);
        return decidedzoneId;
    }


}
