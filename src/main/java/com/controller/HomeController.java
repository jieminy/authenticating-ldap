package com.controller;

import com.Service.impl.UserService;
import com.entity.Msg;
import com.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/doregist",method = RequestMethod.POST)
//    public String doRegist(@Valid User user,Errors errors){
//        try {
//            userService.regist(user);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//        return "re";
//    }

//    @RequestMapping("/getAllUsers")
//    public List<User> getAllUsers(Model model){
//        return userService.getAllUsers();
//    }

    @RequestMapping("/aaaa")
    public String getUser(){
        String roles="";
        List<GrantedAuthority> authorities = ( List<GrantedAuthority> )SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Authority" + grantedAuthority.getAuthority());
            roles+=grantedAuthority.getAuthority();
        }
        return "当前用户："+SecurityContextHolder.getContext().getAuthentication().getName()+" 用户权限: "+roles;
    }

    @RequestMapping(value = "/com", method = RequestMethod.GET)
    @PreAuthorize("authenticated and hasPermission('develplers')")
    public String pemissionValid(){
        return "拥有ROLE_DEVELOPERS权限 可以访问";
    }



}
