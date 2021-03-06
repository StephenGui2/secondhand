package com.wuit.dx.controller;

import com.wuit.dx.entity.LocahAuth;
import com.wuit.dx.entity.PassWd;
import com.wuit.dx.entity.PersonInfo;
import com.wuit.dx.service.LocalAuthService;
import com.wuit.dx.service.PersonInfoService;
import com.wuit.dx.util.ImageUtil;
import com.wuit.dx.util.Page4Navigator;
import com.wuit.dx.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${DX} on 2018/10/27.
 */
@Controller
public class PersonController {

    @Resource
    private LocalAuthService localAuthService;

    @Resource
    private PersonInfoService personInfoService;

    @ResponseBody
    @RequestMapping("/nameverify")
    public Boolean nameIsExists(String username){
        return !localAuthService.isNameExists(username);
    }

    @RequestMapping("/toregister")
    public String register(LocahAuth locahAuth, HttpServletRequest request){
        localAuthService.registerAuth(locahAuth);
        request.getSession().setAttribute("user",locahAuth);
        return "redirect:/";
    }

    @RequestMapping("/tologin")
    public String login(LocahAuth locahAuth,Model model,HttpServletRequest request){
       LocahAuth auth=  localAuthService.loginAuth(locahAuth);
        if(auth!=null){
            if(auth.getEnable()==0){
                model.addAttribute("msg","账号被禁用");
                return "/login";
            }
            request.getSession().setAttribute("user",auth);
            return "redirect:/";
        }else {
            model.addAttribute("msg","账号密码错误");
            return "/login";
        }
    }
    @RequestMapping("/forelogout")
    public String forelogout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }


    @ResponseBody
    @PostMapping("/personinfoes")
    public Object savePersonInfo( MultipartFile profileImg,String []  personinfo, HttpServletRequest request)throws Exception{
        PersonInfo p=new PersonInfo();
        p.setId(Integer.valueOf(personinfo[0]));
        p.setName(personinfo[1]);
        p.setEmail(personinfo[2]);
        p.setGender(personinfo[3]);
        p.setTel(personinfo[4]);
        p.setAddress(personinfo[5]);
        LocahAuth locahAuth=   (LocahAuth)request.getSession().getAttribute("user");
        p.setProfileImg(locahAuth.getUsername());
        PersonInfo   p2=personInfoService.findbyLocalAuth(locahAuth);
        p.setLocalAuth(p2.getLocalAuth());
        personInfoService.savePersonInfo(p);
        ImageUtil.saveOrUpdateImageFile("img/profileImg",p.getLocalAuth().getUsername(),profileImg,request);
        return p;
    }

    @PostMapping("/passWdChange")
    public String passWd(PassWd passWd,Model model, HttpServletRequest request){
        LocahAuth locahAuth=(LocahAuth)request.getSession().getAttribute("user");
        if(!locahAuth.getPassword().equals(passWd.getOldPassWd())){
            model.addAttribute("msg","原始密码错误");
            return "fore/passWd";
        }
        locahAuth.setPassword(passWd.getNewPassWd());
        LocahAuth newAuth= localAuthService.updatePassWd(locahAuth);
        request.getSession().setAttribute("user",newAuth);
        return "redirect:/login";
    }

    @ResponseBody
    @GetMapping("/getAllPerson")
    public Map<String,Object> getAllPerson(@RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size)throws Exception{
        start =start<0?0:start;
        Page4Navigator<PersonInfo> page=personInfoService.findAll(start,size,5);
        Map<String,Object> model=new HashMap<>();
        model.put("page",page);
        return model;
    }

    @ResponseBody
    @PutMapping("/changeEnable")
    public Object changeEnable(int id){
       LocahAuth locahAuth= localAuthService.changeEnable(id);
       return Result.success();
    }
}
