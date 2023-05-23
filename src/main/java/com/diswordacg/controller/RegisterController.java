package com.diswordacg.controller;

import com.diswordacg.mapper.EmailMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Email;
import com.diswordacg.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class RegisterController {

    @Resource
    JavaMailSender javamailSender;

    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private UserMapper userMapper;

    private Email email1;
    private User user;

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
                                @RequestParam(name = "code") String code,
                                HttpServletRequest request){
        request.getSession().setAttribute("email",email);
        request.getSession().setAttribute("username",username);
        user = userMapper.findUserByName(username);
        if (user!=null){
            String register_user;
            register_user="该用户名已被使用";
            request.getSession().setAttribute("register_user",register_user);
            return "ACG_register";
        }
        user = userMapper.findUserByEmail(email);
        if (user!=null){
            String register_email;
            register_email="该邮箱已注册";
            request.getSession().setAttribute("register_email",register_email);
            return "ACG_register";
        }
        if (!code.equals(emailMapper.FindEmailToEmail(email).getCode())){
            String register_code;
            register_code="邮箱验证码错误";
            request.getSession().setAttribute("register_code",register_code);
            return "ACG_register";
        }
        Date date = new Date();
        if (!email1.getTime().after(date)){
            String register_code;
            register_code="验证码已过期";
            request.getSession().setAttribute("register_code",register_code);
            return "ACG_register";
        }

        if (!password1.equals(password2)){
            String register_password;
            register_password = "两次密码输入不一致";
            request.getSession().setAttribute("register_password",register_password);
            return "ACG_register";
        }
        User user1 = new User();
        user1.setU_name(username);
        user1.setU_password(password1);
        user1.setU_email(email);
        userMapper.insert(user1);

        request.getSession().removeAttribute("register_code");
        request.getSession().removeAttribute("register_password");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("email");

        return "redirect:login";
    }

    @GetMapping("/register_code")
    public String emailCode(@RequestParam(name = "username") String username,
                            @RequestParam(name = "email") String email,
                            @RequestParam(name = "password1") String password1,
                            @RequestParam(name = "password2") String password2,
                            HttpServletRequest request) throws ParseException {
        String msg_email;
        request.getSession().setAttribute("email",email);
        request.getSession().setAttribute("username",username);
        request.getSession().setAttribute("psw1",password1);
        request.getSession().setAttribute("psw2",password2);
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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(name);
        message.setSentDate(new Date());
        message.setSubject("[Disword-ACG]注册验证码");

        int ranNum = (int) ((Math.random()*9+1)*100000);
        String code = String.format("%06d",ranNum);
        message.setText("您的验证码是："+ code + "，有效期5分钟，请不要泄漏给别人哦");
        message.setTo(email);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS",calendar);
        email1 = emailMapper.FindEmailToEmail(email);
        if (email1!=null){
            if (email1.getType()!=0){
                emailMapper.UpdateEmailCode(0,code,sdf.parse(time),email);
            }else {
                if (email1.getTime().after(date)){
                    msg_email = "您的验证码仍然有效，请不要重复发送";
                    request.getSession().setAttribute("register_email",msg_email);
                    return "redirect:register";
                }else {
                    emailMapper.UpdateEmailCode(0,code,sdf.parse(time),email);
                }
            }

        }else {
            emailMapper.InsertEmailCode(email,code,0,sdf.parse(time));
        }
        javamailSender.send(message);
        msg_email = "验证码已发送";
        request.getSession().setAttribute("register_email",msg_email);
        return "redirect:register";
    }

}
