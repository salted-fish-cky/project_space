package mapper;

import com.cky.mybatis.beans.User;

import java.util.List;

public interface UserMapper {

    //四个原则：
    //1.接口方法名 == User.xml 中的id名
    //返回值类型与Mapper.xml文件中返回值类型要一致
    //方法的入参类型与Mapper.xml文件中的入参类型要一致

    public User findById(Integer id);


    //根据姓名和名字查询用户
    public List<User> selectUserBySexAndUsername(User user);
}
