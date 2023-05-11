package com.diswordacg.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String Login(){
        return "ACG_login";
    }

    @GetMapping("/user_login")
    public String user_login(HttpServletRequest request,
                             @RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password){
        if (username.equals("123")){
            return "redirect:/";
        }
        String msg_login_user = "用户名不存在";
        request.getSession().setAttribute("msg_login_user",msg_login_user);
        return "redirect:login";


    }
}
