package com.cky.demo;



public class UserDaoTest {
    @org.junit.Test
    public void insertUser() throws Exception {
        UserDao userDao = new UserDao();

        userDao.insertUser(new User("1","1","1","1","1","1"));
    }



}
