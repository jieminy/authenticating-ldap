package com.controller;

import com.Service.impl.UserService;
import com.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class WebMvcConfig {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index() {
        return "home";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/developer")
    public String admin() {
        return "developer";
    }

    @RequestMapping("/tester")
    public String user() {
        return "tester";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/403")
    public String error403() {
        return "/error/403";
    }



    @RequestMapping("/userManage")
    public String getAllUsers(Model model){
        model.addAttribute("users",userService.getAllUsers());
        return "userManage";
    }

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(value = "/register")
    public String register(User user){
        return "registerForm";
    }

    @PostMapping(value = "/register")
    public String doRegist(@Valid User user, Errors errors){
        if(errors.hasErrors()){
            return "/registerForm";
        }
        try {
            userService.regist(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "/registerForm";
        }
        return "redirect:/home";
    }
}
