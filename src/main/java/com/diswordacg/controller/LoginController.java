package com.diswordacg.controller;

import com.diswordacg.mapper.EmailMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Email;
import com.diswordacg.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class LoginController {

    @Value("${spring.mail.username}")
    private String name;

    @Resource
    JavaMailSender javamailSender;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private UserMapper userMapper;

    private Email email1;
    private User user;

    @GetMapping("/login")
    public String Login(){
        return "ACG_login";
    }

    @GetMapping("/user_login")
    public String user_login(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password){
        String login_user;
        String login_password;
        User user;
        if (username.equals("") || username == ""){
            login_user = "用户名不能为空";
            request.getSession().setAttribute("login_user",login_user);
            return "redirect:login";
        }
        user = userMapper.findUserByName(username);
        request.getSession().setAttribute("login_user_value",username);
        if (user == null){
            login_user = "用户名不存在";
            request.getSession().setAttribute("login_user",login_user);
            return "redirect:login";
        }
        request.getSession().removeAttribute("login_user");
        if (password == "" || password.equals("")){
            login_password = "密码不能为空";
            request.getSession().setAttribute("login_password",login_password);
            return "redirect:login";
        }

        if (!user.getU_password().equals(password)){
            login_password = "密码错误";
            request.getSession().setAttribute("login_password",login_password);
            return "redirect:login";
        }
        request.getSession().removeAttribute("login_password");
        request.getSession().removeAttribute("login_user_value");
        response.addCookie(new Cookie("user",user.getU_name()));
        return "redirect:/";


    }

    @GetMapping("/email_login")
    public String email_login(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(name = "email") String email,
                              @RequestParam(name = "code") String code){
        request.getSession().setAttribute("email",email);
        String login_email;
        if (email.equals("") || email == ""){
            login_email = "邮箱不能为空";
            request.getSession().setAttribute("login_email",login_email);
            return "redirect:login";
        }
        email1 = emailMapper.FindEmailToEmail(email);
        if (email1 == null){
            login_email = "该邮箱未注册";
            request.getSession().setAttribute("login_email",login_email);
            return "redirect:login";
        }
        if (email1.getType()!=1){
            login_email = "验证码已失效";
            request.getSession().setAttribute("login_email",login_email);
            return "redirect:login";
        }
        String login_code;
        if (code.equals("") || code == "" ){
            login_code = "验证码不能为空";
            request.getSession().setAttribute("login_code",login_code);
            return "redirect:login";
        }
        Date date = new Date();
        if (!email1.getTime().after(date)){
            login_code = "验证码已过期";
            request.getSession().setAttribute("login_code",login_code);
            return "redirect:login";
        }
        if (!code.equals(email1.getCode())){
            login_code = "验证码错误";
            request.getSession().setAttribute("login_code",login_code);
            return "redirect:login";
        }
        user = userMapper.findUserByEmail(email);
        request.getSession().removeAttribute("login_code");
        request.getSession().removeAttribute("login_email");
        response.addCookie(new Cookie("user",user.getU_name()));
        return "redirect:/";
    }

    @GetMapping("/login_code")
    public String emailCode(@RequestParam(name = "email") String email,
                            HttpServletRequest request) throws ParseException{
        String msg_email;
        if (email==null||email.equals("")){
            msg_email = "邮箱不能为空";
            request.getSession().setAttribute("login_email",msg_email);
            return "redirect:login";
        }
        email1 = emailMapper.FindEmailToEmail(email);
        if (email1 == null){
            msg_email = "该邮箱未注册";
            request.getSession().setAttribute("login_email",msg_email);
            return "redirect:login";
        }

        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$")){
            msg_email = "邮箱格式不正确";
            request.getSession().setAttribute("login_email",msg_email);
            return "redirect:login";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(name);
        message.setSentDate(new Date());
        message.setSubject("[Disword-ACG]登录验证码");

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
            if (email1.getType()!=1){
                emailMapper.UpdateEmailCode(1,code,sdf.parse(time),email);
            }else {
                if (email1.getTime().after(date)){
                    msg_email = "您的验证码仍然有效，请不要重复发送";
                    request.getSession().setAttribute("login_email",msg_email);
                    return "redirect:login";
                }else {
                    emailMapper.UpdateEmailCode(1,code,sdf.parse(time),email);
                }
            }

        }else {
            emailMapper.InsertEmailCode(email,code,1,sdf.parse(time));
        }
        javamailSender.send(message);
        msg_email = "验证码已发送";
        request.getSession().setAttribute("login_email",msg_email);
        request.getSession().setAttribute("email",email);
        return "redirect:login";
    }
}
