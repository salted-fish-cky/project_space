package cn.e3mall.jedis;

import cn.e3mall.common.redis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisTest {

    @Test
    public void jedisTest(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedis = context.getBean(JedisClient.class);
        jedis.set("test","test");
        String str = jedis.get("test");
        System.out.println(str);
    }
}
