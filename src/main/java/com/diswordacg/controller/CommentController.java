package com.diswordacg.controller;

import com.diswordacg.dto.CommentCreateDTO;
import com.diswordacg.dto.CommentDTO;
import com.diswordacg.dto.PostDTO;
import com.diswordacg.dto.ResultDTO;
import com.diswordacg.exception.CustomizeErrorCode;
import com.diswordacg.mapper.CommentMapper;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.mapper.ZoneMapper;
import com.diswordacg.model.Comment;
import com.diswordacg.model.User;
import com.diswordacg.model.Zone;
import com.diswordacg.service.CommentService;
import com.diswordacg.service.PostService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
public class CommentController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private ZoneMapper zoneMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    private User user;

    @GetMapping("/post/{block}/{post_id}")
    public String comment(@PathVariable(name = "block") String block,
                          @PathVariable(name = "post_id") int post_id,
                          Model model, HttpServletRequest request){
        Cookie[] cookies =  request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("user")){
                    user = userMapper.findUserByName(cookie.getValue());
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        PostDTO postDTO = postService.findPostById(post_id);
        List<CommentDTO> commentCreateDTOS = commentService.listByPostId(post_id,1);
//        浏览+1
        if (user != null){
            if (user.getU_id() != postDTO.getCreator_id()){
                postService.AddPostView(post_id,postDTO.getView_count()+1);
            }
        }
        Zone zone = zoneMapper.findZoneToId(postDTO.getZone());
        model.addAttribute("Comment_postDTO",postDTO);
        model.addAttribute("zone",zone.getZone());
        model.addAttribute("comments",commentCreateDTOS);
//        request.getSession().setAttribute("post_title",post.getTitle());
        return "ACG_comment";
    }

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParent_id(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmt_create(new Date());
        comment.setCommentator(user.getU_id());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id){
        List<CommentDTO> commentDTOS = commentService.listByPostId(id,2);
        return ResultDTO.okOf(commentDTOS);
    }


}
