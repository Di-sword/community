package com.diswordacg.controller;

import com.diswordacg.dto.PaginationDTO;
import com.diswordacg.dto.PostDTO;
import com.diswordacg.mapper.BlockMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Block;
import com.diswordacg.model.User;
import com.diswordacg.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private BlockMapper blockMapper;

    @GetMapping("/post/{block}")
    public String post(@PathVariable(name = "block") String block,
                       @RequestParam(name = "page",defaultValue = "1") int page,
                       @RequestParam(name = "size",defaultValue = "6") int size,
                       HttpServletRequest request,
                       Model model){
        request.getSession().removeAttribute("paginationDTO");
        Cookie[] cookies =  request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("user")){
                    User user = userMapper.findUserByName(cookie.getValue());
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        PaginationDTO paginationDTO = postService.findPostDTOByBlock(block,page,size);

        Block block1 = blockMapper.findBlockByBlock(block);

        request.getSession().setAttribute("paginationDTO",paginationDTO);
        request.getSession().setAttribute("block",block1);
        model.addAttribute("",paginationDTO);
        return "ACG_post";
    }




}
