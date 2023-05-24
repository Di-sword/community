package com.diswordacg.mapper;

import com.diswordacg.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
//    注册
    @Insert("insert into user (u_name,u_password,u_email) values (#{u_name},#{u_password},#{u_email})")
    void insert(User user);

    @Select("select * from user")
    List<User> findAllUser();

    @Select("select * from user where u_name=#{u_name}")
    User findUserByName(@Param("u_name") String u_name);

    @Select("select * from user where u_email=#{u_email}")
    User findUserByEmail(String u_email);

    @Select("select * from user where u_id=#{u_id}")
    User findUserById(@Param("u_id") int u_id);

    @Select("select * from user where u_id=#{id}")
    List<User> findUserListById(Integer id);

    @Update("update user set u_name=#{u_name},u_touxiang=#{u_touxiang} where u_id=#{u_id}")
    void updateUser(User user);

    @Update("update user set u_authority=#{u_authority} where u_id=#{u_id}")
    void updateUserAuthority(User user);
}
