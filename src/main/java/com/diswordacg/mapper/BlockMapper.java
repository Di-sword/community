package com.diswordacg.mapper;

import com.diswordacg.model.Plate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlateMapper {

    @Select("select * from plate where zone=#{zone}")
    List<Plate> FindZoneToPlate(int zone);

}
