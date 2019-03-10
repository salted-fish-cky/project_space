package com.jesper.seckill.mapper;

import com.jesper.seckill.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by jiangyunxiong on 2018/5/21.
 */
@Mapper
public interface UserMapper {

    @Select("select * from sk_user where id = #{id}")
    public User getById(@Param("id")long id);

    @Update("update sk_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);

    @Insert("insert into sk_user values(#{id},#{nickname},#{password},#{salt},#{head},#{registerDate},#{lastLoginDate},#{loginCount})")
    public void insert(User user);

    @Update("update sk_user set last_login_date = #{date} where id = #{id}")
    public void updateLoginDateById(@Param("id")long id,@Param("date") Date date);
}
