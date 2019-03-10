package cn.timebusker.mapper;

import cn.timebusker.model.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    @Insert("insert into user1(username, password, nickname, roles) values(#{username}, #{password}, #{nickname}, #{roles})")
    int insert(UserEntity userEntity);

    @Select("select * from user1 where username = #{username}")
    UserEntity selectByUsername(@Param("username") String username);

}
