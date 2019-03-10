package junit;

import com.cky.mybatis.beans.Orders;
import com.cky.mybatis.beans.User;
import mapper.OderMapper;
import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class Test {


   @org.junit.jupiter.api.Test
    public void testMybatis() throws IOException {
//       加载核心配置文件
       InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
       //创建SqlSessionFactory
       SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
       //创建sqlSession
       SqlSession sqlSession = build.openSession();

       //执行sql
       User user = sqlSession.selectOne("test.findById", 10);
       System.out.println(user);

       List<User> users = sqlSession.selectList("test.findListByUsername", "五");
       System.out.println(users);
   }

    @org.junit.jupiter.api.Test
    public void testInsert() throws IOException {
//       加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //执行sql
        User user = new User();
        user.setBirthday(new Date());
        user.setAddress("北京");
        user.setUsername("小红");
        user.setSex("女");
        sqlSession.insert("test.insertUser",user);
        sqlSession.commit();
        System.out.println(user.getId());
    }

    @org.junit.jupiter.api.Test
    public void testUpdate() throws IOException {
//       加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //执行sql
        User user = new User();
        user.setId(27);
        user.setBirthday(new Date());
        user.setAddress("上海");
        user.setUsername("小红");
        user.setSex("女");
        sqlSession.update("test.updateUser",user);
        sqlSession.commit();

    }

    @org.junit.jupiter.api.Test
    public void testDeleteById() throws IOException {
//       加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //执行sql
        sqlSession.delete("test.deleteById",27);
        sqlSession.commit();

    }

@org.junit.jupiter.api.Test
    public void testMapper() throws IOException {
    //加载核心配置文件
    InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
    //创建SqlSessionFactory
    SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
    //创建sqlSession
    SqlSession sqlSession = build.openSession();

    //SqlSession自动生成一个实现类
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.findById(10);
    System.out.println(user);
}

    @org.junit.jupiter.api.Test
    public void testFindUserBySexAndUserName() throws IOException {
        //加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //SqlSession自动生成一个实现类
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
//        user.setSex("1");
        user.setUsername("张小明");
        List<User> users = mapper.selectUserBySexAndUsername(user);
        System.out.println(users);
    }


    @org.junit.jupiter.api.Test
    public void testSelectOrders() throws IOException {
        //加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //SqlSession自动生成一个实现类
        OderMapper mapper = sqlSession.getMapper(OderMapper.class);
        List<Orders> orders = mapper.selectOrders();
        System.out.println(orders);
    }


    @org.junit.jupiter.api.Test
    public void testSelectUserList() throws IOException {
        //加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        //创建sqlSession
        SqlSession sqlSession = build.openSession();

        //SqlSession自动生成一个实现类
        OderMapper mapper = sqlSession.getMapper(OderMapper.class);
        List<User> users = mapper.selectUserList();
        System.out.println(users);
    }

}
