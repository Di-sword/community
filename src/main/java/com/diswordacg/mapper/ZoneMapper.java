package com.diswordacg.mapper;

import com.diswordacg.model.Zone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ZoneMapper {

    @Select("select id from zone where zone=#{zone}")
    Integer findIdToZone(String zone);

    @Select("select zone from zone where id=#{id}")
    Zone findZoneToId(@Param("id") Integer id);



}
