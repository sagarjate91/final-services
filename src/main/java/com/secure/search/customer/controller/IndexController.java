package com.secure.search.customer.controller;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.service.ConstantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class IndexController {

    @GetMapping({"/","/home.htm"})
    public String index(Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickHome",true);
        model.addAttribute("title", "Home");
        return "page";
    }

    @GetMapping({"/login","/user.htm"})
    public String loginUser(Model model,@ModelAttribute("message") String message){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("title", "Login Page");
        model.addAttribute("userClickUser",true);
        model.addAttribute("action","customer/login-validate");
        model.addAttribute("command",new UserModel());
        if(message!=null){
            model.addAttribute("message",message+"");
        }
        return "page";
    }

    @GetMapping("/error")
    public String error(){
        return "error.jsp";
    }


    @GetMapping({"/signup","/registerPanel.htm"})
    public String signup(Model model, @ModelAttribute("message") String message){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("title", "Signup Page");
        model.addAttribute("userClickRegister",true);
        model.addAttribute("action","customer/signup-add");
        model.addAttribute("command",new UserModel());
        if(message!=null){
            model.addAttribute("message",message+"");
        }
        return "page";
    }

    @GetMapping({"/adminPanel.htm"})
    public String adminUser(@ModelAttribute("message")String message,Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("title", "Admin Page");
        model.addAttribute("userClickAdmin",true);
        model.addAttribute("action","admin/admin-validate");
        model.addAttribute("command",new UserModel());
        if(message!=null){
        	model.addAttribute("message", message);
        }
        return "page";
    }
}
