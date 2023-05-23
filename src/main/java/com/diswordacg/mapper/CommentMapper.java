package com.diswordacg.mapper;

import com.diswordacg.model.Comment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment (parent_id,type,commentator,gmt_create,content) values" +
            " (#{parent_id},#{type},#{commentator},#{gmt_create},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{id}")
    Comment findByParentId(@Param("id")Long id);

}
