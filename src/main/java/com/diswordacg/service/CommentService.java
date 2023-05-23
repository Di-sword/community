package com.diswordacg.service;

import com.diswordacg.enums.CommentTypeEnum;
import com.diswordacg.exception.CustomizeErrorCode;
import com.diswordacg.exception.CustomizeException;
import com.diswordacg.mapper.CommentMapper;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.model.Comment;
import com.diswordacg.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostMapper postMapper;

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
            commentMapper.insert(comment);
            postMapper.AddPostComment(comment.getParent_id());
        }
    }
}
