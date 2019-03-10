package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit融合，junit启动时加载springIOC容器
 * Created by Administrator on 2017/6/7/007.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {


    //注入DAO实现依赖

    @Resource
    private SeckillDao seckillDao;


    @Test
    public void testReduceNumber() throws Exception{
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1L,killTime);
        System.out.println("updateCount="+updateCount);
    }

    @Test
    public void testQueryById() throws Exception{
        long id  =1;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll() throws Exception{
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill:seckills
             ) {
            System.out.println(seckill);
        }
    }


}
