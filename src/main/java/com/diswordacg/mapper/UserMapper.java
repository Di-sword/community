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

}
