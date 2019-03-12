package com.wuit.dx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author dx
 * @since 2019/3/11 10:46
 */
@Controller
public class ForePageController {

    @RequestMapping("/personcenter")
    public String personCenter(){
        return "fore/personcenter";
    }

    @GetMapping("/")
    public String index(){
        return  "redirect:home";
    }

    @GetMapping("/home")
    public String home(){
        return "fore/home";
    }
}
