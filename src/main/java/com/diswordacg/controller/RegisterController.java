package com.diswordacg.controller;

import com.diswordacg.mapper.EmailMapper;
import com.diswordacg.model.Email;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class RegisterController {

    @Resource
    JavaMailSender javamailSender;

    @Autowired
    private EmailMapper emailMapper;


    @Value("${spring.mail.username}")
    private String name;

    @GetMapping("/register")
    public String Login(){
        return "ACG_register";
    }

    @GetMapping("/user_register")
    public String user_register(@RequestParam(name = "username") String username,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "password1") String password1,
                                @RequestParam(name = "password2") String password2,
                                HttpServletRequest request){
        String a = "123";
        request.getSession().setAttribute("msg_register_user",a);
        request.getSession().setAttribute("register_code",a);
        request.getSession().setAttribute("register_password1",a);
        request.getSession().setAttribute("register_password2",a);


        return "0";
    }

    @GetMapping("/register_code")
    public String emailCode(@RequestParam(name = "email") String email,
                          HttpServletRequest request){
        String msg_email;
        if (email==null||email.equals("")){
            msg_email = "邮箱不能为空";
            request.getSession().setAttribute("register_email",msg_email);
            return "redirect:register";
        }

        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$")){
            msg_email = "邮箱格式不正确";
            request.getSession().setAttribute("register_email",msg_email);
            return "redirect:register";
        }
        Email email1 = emailMapper.FindEmailToEmail(email);
        if (email1!=null){
            msg_email="该邮箱已注册";
            request.getSession().setAttribute("register_email",msg_email);
            return "redirect:register";
        }

        Date now = new Date();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(name);
        message.setSubject("芜湖起飞");
        message.setText("家人们，邮箱验证终于他妈的弄好了");
        message.setTo(email);
        javamailSender.send(message);

//        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        return "redirect:login";
    }

}
