package com.diswordacg.controller;

import com.diswordacg.dto.PaginationDTO;
import com.diswordacg.dto.PostDTO;
import com.diswordacg.mapper.BlockMapper;
import com.diswordacg.mapper.PostMapper;
import com.diswordacg.mapper.UserMapper;
import com.diswordacg.mapper.ZoneMapper;
import com.diswordacg.model.Block;
import com.diswordacg.model.Post;
import com.diswordacg.model.User;
import com.diswordacg.service.PostService;
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
public class ZhuController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ZoneMapper zoneMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;

    private User user;

    @GetMapping("/")
    public String Zhu(HttpServletRequest request){
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
        List<Block> list1 = null;
        List<Block> list2 = null;
        List<Block> list3 = null;
        List<Block> list4 = null;
        list1 = blockMapper.FindZoneToPlate(1);
        list2 = blockMapper.FindZoneToPlate(2);
        list3 = blockMapper.FindZoneToPlate(3);
        list4 = blockMapper.FindZoneToPlate(4);
        request.getSession().setAttribute("game_list1",list1);
        request.getSession().setAttribute("game_list2",list2);
        request.getSession().setAttribute("game_list3",list3);
        request.getSession().setAttribute("game_list4",list4);

        return "ACG_Zhu";
    }

//    登出，删除所有cookie
    @GetMapping("/loginOut")
    private String loginOut(HttpServletResponse response,
                            HttpServletRequest request){

        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "redirect:login";
    }

    @GetMapping("/selectPost")
    private String selectPost(@RequestParam(name = "select") String selectName,
                              @RequestParam(name = "page",defaultValue = "1") Integer page,
                              @RequestParam(name = "size",defaultValue = "7") Integer size,
                              Model model){
//        if (selectName == null){
//            return "redirect:/";
//        }
        PaginationDTO selectList = postService.select(selectName,page,size);
        List<Post> selectPostSize = postMapper.findPostByTitle2("%"+selectName+"%");
        model.addAttribute("selectPostSize",selectPostSize);
        model.addAttribute("selectList",selectList);
        model.addAttribute("selectName",selectName);
        return "ACG_select";
    }



}
