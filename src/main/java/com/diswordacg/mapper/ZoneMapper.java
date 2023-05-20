package com.diswordacg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ZomeMapper {

    @Select("select id from zone where zone=#{zone}")
    int findZoneToId(String zone);


}
