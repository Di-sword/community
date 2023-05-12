package com.diswordacg.mapper;

import com.diswordacg.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
//    注册
    @Insert("insert into user (u_name,u_password,u_email) values (#{u_name},#{u_password},#{u_email})")
    void insert(User user);

    @Select("select * from user where u_name=#{u_name}")
    User FindNameToUser(String u_name);

    @Select("select * from user where u_email=#{u_email}")
    User FindEmailToUser(String u_email);


}
