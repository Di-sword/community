package com.diswordacg.controller;

import com.diswordacg.mapper.BlockMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.mapper.ZoneMapper;
import com.diswordacg.model.Block;
import com.diswordacg.model.User;
import com.diswordacg.model.Zone;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AddBlockController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private ZoneMapper zoneMapper;

    private User user;

    @GetMapping("addblock")
    public String AddBlock(HttpServletRequest request){
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
        if (user==null){
            return "login";
        }else if (user.getU_authority()<1){
            return "/";
        }

        return "ACG_addblock";
    }

    @PostMapping(value = "/add_block", consumes = "multipart/form-data")
    public String Add_Block(HttpServletRequest request,
                            @RequestParam(name = "file") MultipartFile file,
                            @RequestParam(name = "content") String content,
                            @RequestParam(name = "fenqu") String zone,
                            @RequestParam(name = "block") String block){
        request.getSession().setAttribute("addblock_content",content);
        request.getSession().setAttribute("addblock_block",block);
        String error = "";
        if (zone.equals("") || zone == ""){
            error = "分区不能为空";
            request.getSession().setAttribute("addblock_error",error);
            return "redirect:addblock";
        }
        if (content.equals("") || content == ""){
            error = "简介不能为空";
            request.getSession().setAttribute("addblock_error",error);
            return "redirect:addblock";
        }
        if (block.equals("") || block == ""){
            error = "板块不能为空";
            request.getSession().setAttribute("addblock_error",error);
            return "redirect:addblock";
        }
        if (zone.equals("动漫") || zone == "动漫"){
            block += "(A)";
        }
        if (content.length() < 15 ){
            error = "简介内容不能小于十五个字";
            request.getSession().setAttribute("addblock_error",error);
            return "redirect:addblock";
        }

        String img_name = file.getOriginalFilename(); //图片原名
        if (img_name.equals("") || img_name == null){
            error = "板块图片不能为空";
            request.getSession().setAttribute("addblock_error",error);
            return "redirect:addblock";
        }
        String ext = "." + img_name.split("\\.")[1];
        if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".gif")){
            error = "不支持的文件类型";
            request.getSession().setAttribute("addblock_error",error);
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

        Block block0 = new Block();
        block0.setZone(zoneMapper.findIdToZone(zone));
        block0.setBlock(block);
        block0.setIntroduce(content);
        block0.setIcon(fileName);
        blockMapper.AddBlock(block0);
        error = "添加成功";
        request.getSession().setAttribute("addblock_error",error);
        request.getSession().removeAttribute("addblock_content");
        request.getSession().removeAttribute("addblock_block");
        return "redirect:addblock";
    }

}
