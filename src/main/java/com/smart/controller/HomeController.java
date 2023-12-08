package com.smart.controller;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
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
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, Model model, @RequestParam(value="agreement", defaultValue = "false") boolean agreement,   HttpSession session){

        try {


            if (result1.hasErrors()){
                System.out.println("ERROR"+result1.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            if (!agreement){
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("https://cemhri.org/wp-content/uploads/2018/04/Home-Four-Banner-Background-Image.png");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            System.out.println("Agreement" +agreement);
            System.out.println("USER" +user);

            User result=this.userRepository.save(user);
            model.addAttribute("user",new User());

            session.setAttribute("message", new Message("Successfully register !!" , "alert-success"));
            return "signup";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Somthing went wrong!!" +e.getMessage(), "alert-danger"));
            return "signup";
        }

    }

    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("Title","Login Page");
        return "login";
    }
}
