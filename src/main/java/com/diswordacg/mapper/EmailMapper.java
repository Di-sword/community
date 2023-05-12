package com.diswordacg.mapper;

import com.diswordacg.model.Email;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface EmailMapper {

    @Select("select * from email_code where email=#{email}")
    Email FindEmailToEmail(String email);

    @Insert("insert into email_code(email,code,type,time) values (#{email},#{code},#{type},#{time})")
    void InsertEmailCode(String email, String code, int type, Date time);

    @Update("update email_code set type=#{type},code=#{code},time=#{time} where email=#{email}")
    void UpdateEmailCode(int type,String code,Date time,String email);
}
