package com.diswordacg.mapper;

import com.diswordacg.dto.PostDTO;
import com.diswordacg.model.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Insert("insert into post (title,content,zone,block,img,creator_id,creator_name,release_time) values " +
            "(#{title},#{content},#{zone},#{block},#{img},#{creator_id},#{creator_name},#{release_time})")
    void insertPost(Post post);

    @Update("update post set view_count = #{view_count} where id=#{id}")
    void AddPostView(Post post);
    @Update("update post set comment_count = comment_count+1 where id = #{id}")
    void AddPostComment(@Param("id") Long id);

    @Select("select * from post where block=#{block} limit #{offset},#{size}")
    List<Post> findPostByBlock(@Param(value = "block") String block,@Param(value = "offset") int offset,@Param(value = "size") int size);

    @Select("select * from post where block=#{block}")
    List<Post> findPostByBlock2(@Param(value = "block") String block);

    @Select("select * from post where creator_id=#{creator_id} limit #{offset},#{size}")
    List<Post> findPostByIdlist(@Param(value = "creator_id") Integer id,@Param(value = "offset") int offset,@Param(value = "size") int size);

    @Select("select * from post where creator_id=#{creator_id}")
    List<Post> findPostById2(@Param(value = "creator_id") Integer id);

    @Select("select * from post where title like #{title} limit #{offset},#{size}")
    List<Post> findPostByTitle(@Param(value = "title") String title,@Param(value = "offset") int offset,@Param(value = "size") int size);

    @Select("select * from post where title like #{title}")
    List<Post> findPostByTitle2(@Param(value = "title") String title);

    @Select("select * from post where id=#{id}")
    Post findPostById(@Param("id") Integer id);

    @Select("select * from post where id=#{id}")
    Post findPostByLongId(@Param("id") Long id);


    @Select("select count(1) from post where block=#{block}")
    Integer count(@Param(value = "block") String block);

    @Select("select count(1) from post where creator_id=#{creator_id}")
    Integer countById(@Param(value = "creator_id") Integer id);

    @Select("select count(1) from post where title like #{title}")
    Integer countByTitle(@Param(value = "title")String title);




}
