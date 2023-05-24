package com.diswordacg.controller;

import com.diswordacg.dto.PaginationDTO;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Post;
import com.diswordacg.model.User;
import com.diswordacg.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ProfileController {

    private Cookie[] cookies;
    private User user;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;

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
                        break;
                    }
                }
            }
        }

        if (user == null){
            return "redirect:/";
        }else {
            if ("posts".equals(action)){
                model.addAttribute("section","posts");
                model.addAttribute("sectionName","我的帖子");
            } else if ("user".equals(action)){
                model.addAttribute("section","user");
                model.addAttribute("sectionName","个人信息");
            }

            PaginationDTO postDTO = postService.findPostDTOById(user.getU_id(), page, size);
            List<Post> postSize = postMapper.findPostById2(user.getU_id());
            model.addAttribute("postDTO",postDTO);
            model.addAttribute("postSize",postSize);
            return "ACG_Profile";
        }
    }

    @PostMapping(value = "/updateUser", consumes = "multipart/form-data")
    public String updateUser(@RequestParam(name = "file") MultipartFile file,
                             @RequestParam(name = "username") String username,
                             Model model,HttpServletRequest request){
        cookies =  request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("user")){
                    user = userMapper.findUserByName(cookie.getValue());
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                        break;
                    }
                }
            }
        }
        String error = "";
        String img_name = file.getOriginalFilename();
        if (user == null){
            return "/";
        }
        if (!file.isEmpty()){
            String ext = "." + img_name.split("\\.")[1];
            if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".gif")){
                error = "不支持的文件类型";
                request.getSession().setAttribute("updateuser_error",error);
                return "redirect:/profile/user";
            }
            String uuid = UUID.randomUUID().toString().replace("-","");
            String fileName = uuid+ext;
            ApplicationHome applicationHome = new ApplicationHome(this.getClass());
            String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                    "\\src\\main\\resources\\static\\img\\";
            String path = pre + fileName;
            try {
                file.transferTo(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setU_touxiang(fileName);
        }
        if (username == "" || username.equals("")){
            error = "用户名不能为空";
            request.getSession().setAttribute("updateuser_error",error);
            return "redirect:/profile/user";
        }else if (username != user.getU_name() || !username.equals(user.getU_name())){
            User user1 = userMapper.findUserByName(username);
            if (user1 != null && user.getU_name().equals(username)){
                user.setU_name(username);
            }else {
                error="用户名已被使用";
                request.getSession().setAttribute("updateuser_error",error);
                return "redirect:/profile/user";
            }
        }
        userMapper.updateUser(user);
        request.getSession().removeAttribute("updateuser_error");
        return "redirect:/profile/user";
    }



}
