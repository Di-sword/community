package com.diswordacg.controller;

import com.diswordacg.dto.PaginationDTO;
import com.diswordacg.mapper.UserMapper;
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

@Controller
public class ProfileController {

    private Cookie[] cookies;
    private User user;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostService postService;

    @GetMapping("/profile/{action}")
    public String Profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "7") Integer size,
                          HttpServletRequest request,
                          Model model){
        cookies =  request.getCookies();
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

        if (user == null){
            return "redirect:/";
        }
        if ("posts".equals(action)){
            model.addAttribute("section","posts");
            model.addAttribute("sectionName","我的帖子");
        } else if ("user".equals(action)){
            model.addAttribute("section","user");
            model.addAttribute("sectionName","个人信息");
        }

        PaginationDTO postDTO = postService.findPostDTOById(user.getU_id(), page, size);
        model.addAttribute("postDTO",postDTO);
        return "ACG_Profile";
    }



}
