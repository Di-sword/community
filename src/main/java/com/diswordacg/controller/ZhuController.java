package com.diswordacg.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ZhuController {
    @GetMapping("/")
    public String Zhu(){
        return "ACG_Zhu";
    }

}
