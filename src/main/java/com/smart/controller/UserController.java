package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

//    method for adding conmmond data for responce
    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String userName=principal.getName();
        System.out.println("user name >"+userName);
        User user=userRepository.getUserByUserName(userName);
        System.out.println("user  >"+user);
        model.addAttribute("user",user);

    }
//  Dashboard home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title", "user dashboard");
        return "normal/user_dashboard";
    }

//   open Add form Handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String proccessContact(@ModelAttribute Contact contact, Principal principal){
        String name=principal.getName();
        User user=userRepository.getUserByUserName(name);
        contact.setUser(user);
        user.getContacts().add(contact);
        this.userRepository.save(user);
        System.out.println("Data >>>>"+contact);
        return "normal/add_contact_form";
    }
}
