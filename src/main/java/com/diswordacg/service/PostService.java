package com.diswordacg.service;

import com.diswordacg.dto.PaginationDTO;
import com.diswordacg.dto.PostDTO;
import com.diswordacg.exception.CustomizeException;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.mapper.ZoneMapper;
import com.diswordacg.model.Post;
import com.diswordacg.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ZoneMapper zoneMapper;

    public PaginationDTO findPostDTOByBlock(String block, int page, int size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = postMapper.count(block);
        Integer totalPage;
        if (totalCount%size == 0 || totalCount<size){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page=totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        int offset = size * (page - 1);
        List<Post> postList = new ArrayList<>();
        if (totalCount < size){
            postList = postMapper.findPostByBlock2(block);
        }else {
            postList = postMapper.findPostByBlock(block,offset,size);
        }

        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : postList) {
            User user = userMapper.findUserById(post.getCreator_id());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post,postDTO);
            postDTO.setUser(user);
            postDTOList.add(postDTO);
        }
        paginationDTO.setPostDTOList(postDTOList);

        return paginationDTO;

    }

    public PaginationDTO findPostDTOById(int id, int page, int size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = postMapper.countById(id);
        Integer totalPage;
        if (totalCount%size == 0 || totalCount<size){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        int offset = size * (page - 1);
        List<Post> postList = new ArrayList<>();
        if (totalCount < size){
            postList = postMapper.findPostById2(id);
        }else {
            postList = postMapper.findPostByIdlist(id,offset,size);
        }

        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : postList) {
            User user = userMapper.findUserById(post.getCreator_id());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post,postDTO);
            postDTO.setUser(user);
            postDTOList.add(postDTO);
        }
        paginationDTO.setPostDTOList(postDTOList);

        return paginationDTO;

    }

    public PostDTO findPostById(int post_id) {
        Post post = postMapper.findPostById(post_id);
        if (post == null){
            throw new CustomizeException("帖子 #"+post_id+"不存在");
        }
        User user = userMapper.findUserById(post.getCreator_id());
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post,postDTO);
        postDTO.setUser(user);
        return postDTO;
    }

    public void AddPostView(int post_id,int view_count){
        Post post = postMapper.findPostById(post_id);
        post.setView_count(view_count);
        postMapper.AddPostView(post);
    }

    public PaginationDTO select(String selectName,Integer page,Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = postMapper.countByTitle("%"+selectName+"%");
        Integer totalPage;
        if (totalCount%size == 0 || totalCount<size){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        int offset = size * (page - 1);
        List<Post> postList = new ArrayList<>();
        if (totalCount < size){
            postList = postMapper.findPostByTitle2("%"+selectName+"%");
        }else {
            postList = postMapper.findPostByTitle("%"+selectName+"%",offset,size);
        }
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : postList) {
            User user = userMapper.findUserById(post.getCreator_id());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post,postDTO);
            postDTO.setUser(user);
            postDTOList.add(postDTO);
        }
        paginationDTO.setPostDTOList(postDTOList);

        return paginationDTO;

    }
}
