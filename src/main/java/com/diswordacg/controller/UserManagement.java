package com.diswordacg.controller;

import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserManagement {

    private User user;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/userManagement")
    public String userManagement(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model){
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
        if (user.getU_authority() != 2){
            return "redirect:/";
        }
        List<User> userList = userMapper.findAllUser();
        request.getSession().setAttribute("userList",userList);
        return "ACG_user";
    }

    @GetMapping("/authority_update")
    public String updateAuthority(HttpServletRequest request,
                                   @RequestParam(name = "id") Integer id,
                                   @RequestParam(name = "authority") Integer authority){
        if (authority<0 || authority>1){
            return "redirect:userManagement";
        }
        if (id == 1){
            return "redirect:userManagement";
        }
        User user = userMapper.findUserById(id);
        user.setU_authority(authority);
        userMapper.updateUserAuthority(user);
        return "redirect:userManagement";
    }

}
