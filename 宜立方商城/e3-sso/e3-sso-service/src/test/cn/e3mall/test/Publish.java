package cn.e3mall.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Publish {
    @Test
    public void testPublish() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        System.out.println("服务已经启动");
        System.in.read();
        System.out.println("服务已经关闭");

    }
}
