package com.diswordacg.controller;

import com.diswordacg.mapper.BlockMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.model.Block;
import com.diswordacg.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UpdateBlockController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlockMapper blockMapper;

    private User user;

    @GetMapping("updateblock")
    public String updateblock(HttpServletRequest request){
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



        return "ACG_updateblock";
    }


    @PostMapping(value = "update_block", consumes = "multipart/form-data")
    private String update_block(HttpServletRequest request,
                                @RequestParam(name = "fenqu") String zone,
                                @RequestParam(name = "bankuai") String block0,
                                @RequestParam(name = "block") String block,
                                @RequestParam(name = "content") String introduce,
                                @RequestParam(name = "file") MultipartFile file){
        request.getSession().setAttribute("updateblock_introduce",introduce);
        request.getSession().setAttribute("updateblock_block",block);
        String error;
        if (zone.equals("") || zone == ""){
            error = "请选择分区";
            request.getSession().setAttribute("updateblock_error",error);
            return "redirect:updateblock";
        }
        if (block0.equals("") || block0 ==""){
            error = "请选择需要更改的板块";
            request.getSession().setAttribute("updateblock_error",error);
            return "redirect:updateblock";
        }
        Block block1 = blockMapper.findBlockByBlock(block0);
        if (!block.equals("") || block != ""){
            block1.setBlock(block);
        }
        if (!introduce.equals("") || introduce != ""){
            block1.setIntroduce(introduce);
        }
        String img_name = file.getOriginalFilename();
        if (!file.isEmpty()){
            String ext = "." + img_name.split("\\.")[1];
            if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".gif")){
                error = "不支持的文件类型";
                request.getSession().setAttribute("updateblock_error",error);
                return "redirect:updateblock";
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
            block1.setIcon(fileName);
        }

        blockMapper.updateBlock(block1);
        error = "修改成功";
        request.getSession().setAttribute("updateblock_error",error);
        request.getSession().removeAttribute("updateblock_introduce");
        request.getSession().removeAttribute("updateblock_block");
        return "redirect:updateblock";
    }

}
