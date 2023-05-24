package com.diswordacg.service;

import com.diswordacg.dto.CommentDTO;
import com.diswordacg.enums.CommentTypeEnum;
import com.diswordacg.exception.CustomizeErrorCode;
import com.diswordacg.exception.CustomizeException;
import com.diswordacg.mapper.CommentMapper;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Comment;
import com.diswordacg.model.Post;
import com.diswordacg.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParent_id() == null || comment.getParent_id() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment dbComment = commentMapper.findByParentId(comment.getParent_id());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
//            回复帖子
            Post post = postMapper.findPostByLongId(comment.getParent_id());
            if (post == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND2);
            }
            postMapper.AddPostComment(comment.getParent_id());
            commentMapper.insert(comment);
        }
    }

    public List<CommentDTO> listByPostId(int post_id,int type) {
        List<CommentDTO> commentDTOS = new ArrayList<>();

        List<Comment> comments = commentMapper.findListByParentId(post_id,type);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        for (Comment comment : comments) {
            User user = userMapper.findUserById(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            commentDTOS.add(commentDTO);
        }

        return commentDTOS;
    }
}
