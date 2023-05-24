package com.diswordacg.controller;

import com.diswordacg.mapper.BlockMapper;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.mapper.ZoneMapper;
import com.diswordacg.model.Block;
import com.diswordacg.model.Post;
import com.diswordacg.model.User;
import com.vdurmont.emoji.EmojiParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class PublishController {

    private List<Block> plates;
    private User user;
    private Cookie[] cookies;

    @Autowired
    private BlockMapper plateMapper;
    @Autowired
    private ZoneMapper zoneMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request){
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
        return "ACG_publish";
    }

    @PostMapping(value = "/addpost", consumes = "multipart/form-data")
    public String addPost(HttpServletRequest request,
                          @RequestParam(name = "title")String title,
                          @RequestParam(name = "fenqu")String zone,
                          @RequestParam(name = "bankuai")String block,
                          @RequestParam(name = "content")String content,
                          @RequestParam(name = "file") MultipartFile file
    ){
        request.getSession().setAttribute("publish_title",title);
        request.getSession().setAttribute("publish_content",content);
        Post post0 = new Post();
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
        String error;
        if (user == null){
            error = "用户未登录，不能发帖";
            request.getSession().setAttribute("publish_error",error);
            return "redirect:publish";
        }
        if (title == null || title.equals("")){
            error = "标题不能为空";
            request.getSession().setAttribute("publish_error",error);
            return "redirect:publish";
        }
        if (content.length()<=10){
            error = "内容字数太少了";
            request.getSession().setAttribute("publish_error",error);
            return "redirect:publish";
        }

        String img_name = file.getOriginalFilename(); //图片原名
        if (img_name != "" || !img_name.equals("")){
            String ext = "." + img_name.split("\\.")[1];
            if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".gif")){
                error = "不支持的文件类型";
                request.getSession().setAttribute("publish_error",error);
                return "redirect:publish";
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
            post0.setImg(fileName);
        }
//        System.out.println(path);

        int z = zoneMapper.findIdToZone(zone);
        Date date = new Date();
        post0.setCreator_id(user.getU_id());
        post0.setCreator_name(user.getU_name());
        post0.setTitle(title);
        post0.setZone(z);
        post0.setBlock(block);
        post0.setContent(content);
        post0.setRelease_time(date);
        postMapper.insertPost(post0);
        request.getSession().removeAttribute("publish_title");
        request.getSession().removeAttribute("publish_content");
        error = "发布成功";
        request.getSession().setAttribute("publish_error",error);
        return "redirect:publish";
    }


    @RequestMapping("/getBankuai")
    @ResponseBody
    public List<Block> getBankuai(HttpServletRequest request){
        int id = zoneMapper.findIdToZone(request.getParameter("fenqu"));
        plates = plateMapper.FindZoneToPlate(id);
        return plates;
    }

}
