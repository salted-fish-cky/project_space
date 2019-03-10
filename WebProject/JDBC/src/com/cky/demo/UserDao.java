package com.cky.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private List<User> userList;

    public List<User> getAllUser(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String url = "jdbc:mysql://localhost:3306/test";
        String password = "123456";
        String user = "root";
        String driverClass = "com.mysql.jdbc.Driver";
        String sql = "select * from user";


        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url,user,password);
            resultSet = connection.prepareStatement(sql).executeQuery();
            User u = null;
            userList = new ArrayList<>();
            while(resultSet.next()){
               u = new User();
//               u.setId(resultSet.getInt(resultSet.getInt("id")));
//                u.setAccount(resultSet.getString(resultSet.getInt("account")));
//                u.setPassword(resultSet.getString(resultSet.getInt("password")));
//                u.setNickName(resultSet.getString(resultSet.getInt("nickname")));
//                u.setGender(resultSet.getString(resultSet.getInt("gender")));
//                u.setTel(resultSet.getInt(resultSet.getInt("tel")));
//                u.setEmail(resultSet.getString(resultSet.getInt("email")));
                u.setId(resultSet.getInt(1));
                u.setAccount(resultSet.getString(2));
                u.setPassword(resultSet.getString(3));
                u.setNickName(resultSet.getString(4));
                u.setGender(resultSet.getString(5));
                u.setTel(resultSet.getString(6));
                u.setEmail(resultSet.getString(7));
                userList.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

                try {
                    if(connection!=null){
                        connection.close();
                    }
                    if(preparedStatement!=null){
                        preparedStatement.close();
                    }
                    if(resultSet!=null){
                        resultSet.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }
        return userList;
    }

    public int insertUser(User u){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String url = "jdbc:mysql://localhost:3306/test?useSSL=true";
        String password = "123456";
        String user = "root";
        String driverClass = "com.mysql.jdbc.Driver";
        String sql = "insert into user (account,password,nickname,gender,tel,email) values(?,?,?,?,?,?)";
//        String sql = "insert into student (name,age,address,phone,studentcol) values(?,?,?,?,?)";
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url,user,password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,u.getAccount());
            preparedStatement.setString(2,u.getPassword());
            preparedStatement.setString(3,u.getNickName());
            preparedStatement.setString(4,u.getGender());
            preparedStatement.setString(5,u.getTel());
            preparedStatement.setString(6,u.getEmail());
            result = preparedStatement.executeUpdate();
            if(result == 1){
                System.out.println("插入成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void deleteUserById(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String url = "jdbc:mysql://localhost:3306/mall";
        String password = "1234";
        String user = "root";
        String driverClass = "com.mysql.jdbc.Driver";
        String sql = "delete from user where id=?";
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url,user,password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
