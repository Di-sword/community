package com.diswordacg.mapper;

import com.diswordacg.model.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmailMapper {

    @Select("select * from email_code where email=#{email}")
    Email FindEmailToEmail(String email);

    @Select("select * from ")
    Email FindEmailToEM(Email email);

}
