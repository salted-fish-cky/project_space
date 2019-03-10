package com.cky.mybatis.test;

import com.cky.mybatis.mapper.UserMapper;
import com.cky.mybatis.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JunitTest {

    @Test
    public void testMapper(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = userMapper.findById(1);
        System.out.println(user);
    }
}
