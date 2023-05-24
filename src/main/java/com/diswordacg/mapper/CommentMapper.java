package com.diswordacg.mapper;

import com.diswordacg.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment (parent_id,type,commentator,gmt_create,content) values" +
            " (#{parent_id},#{type},#{commentator},#{gmt_create},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{id}")
    Comment findByParentId(@Param("id")Long id);

    @Select("select * from comment where parent_id=#{parent_id} and type=#{type}")
    List<Comment> findListByParentId(@Param("parent_id") Integer parent_id,@Param("type") Integer type);

}
