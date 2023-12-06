package com.smart.controller;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "about - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("Register", "about - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value="/do_register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user, @RequestParam(value="agreement", defaultValue = "false") boolean agreement, Model model){
        System.out.println("Agreement" +agreement);
        System.out.println("USER" +user);
        return "signup";
    }
}
