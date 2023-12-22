package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.helper.Message;
import com.smart.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    Random random = new Random(1000);

//    email id form handler
    @RequestMapping("/forgot")
    public String openEmailForm(){
        return "forgot_email_form";
    }


    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession httpSession, Principal principal){

//        String name = principal.getName();
//        String userEmail1 = this.userRepository.getUserByUserName(name).getEmail();
//        String Mailto="";
//        if (userEmail1.equalsIgnoreCase(email)){
//            Mailto=email;
//        }

//        generating 4 digit random number
        int otp = random.nextInt(999999);
        System.out.println("OTP - "+otp);

//        write code for send otp to email
        String Mailto=email;
        String Subject="OTP from SCM";
        String Body=" <h1> OTP = "+otp+"</h1>";


        boolean flag = this.emailService.sendMail(Mailto, Subject, Body);
        if (flag){
            httpSession.setAttribute("otp",otp);
//            httpSession.setAttribute("message", new Message("OTP sent !!","alert-seccess"));
            return "verify_otp";
        }else {
            httpSession.setAttribute("message", new Message("Check your email id !!","alert-danger"));
            return "forgot_email_form";
        }


    }

}
