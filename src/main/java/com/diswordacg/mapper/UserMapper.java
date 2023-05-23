package com.diswordacg.mapper;

import com.diswordacg.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
//    注册
    @Insert("insert into user (u_name,u_password,u_email) values (#{u_name},#{u_password},#{u_email})")
    void insert(User user);

    @Select("select * from user where u_name=#{u_name}")
    User findUserByName(@Param("u_name") String u_name);

    @Select("select * from user where u_email=#{u_email}")
    User findUserByEmail(String u_email);

    @Select("select * from user where u_id=#{u_id}")
    User findUserById(@Param("u_id") int u_id);


}
