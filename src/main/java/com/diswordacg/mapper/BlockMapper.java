package com.diswordacg.mapper;

import com.diswordacg.model.Block;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlockMapper {

    @Select("select * from block where zone=#{zone}")
    List<Block> FindZoneToPlate(int zone);

    @Insert("insert into block (block,zone,icon,introduce) values (#{block},#{zone},#{icon},#{introduce})")
    void AddBlock(Block block);

    @Select("select * from block where block=#{block0}")
    Block findBlockByBlock(@Param("block0") String block0);

    @Update("update block set block=#{block},zone=#{zone},icon=#{icon},introduce=#{introduce} where id=#{id}")
    void updateBlock(Block block);
}
