package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotController {
    Random random = new Random(1000);

//    email id form handler
    @RequestMapping("/forgot")
    public String openEmailForm(){
        return "forgot_email_form";
    }


    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email){

//        generating 4 digit random number
        int otp = random.nextInt(999999);
        System.out.println("OTP - "+otp);

//        write code for send otp to email

        return "verify_otp";
    }

}
