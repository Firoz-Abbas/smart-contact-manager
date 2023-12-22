package com.smart.service;

import com.smart.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private UserRepository userRepository;

    public EmailServiceImpl(){}

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public boolean sendMail(String mailto, String subject, String body) {
        boolean f=false;
        try {
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromMail);
            mimeMessageHelper.setTo(mailto);
//            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body,true);
//            mimeMessageHelper.setText(body);

//            for (int i=0; i<mailfile.length; i++){
//                mimeMessageHelper.addAttachment(
//                        mailfile[i].getOriginalFilename(),
//                        new ByteArrayResource(mailfile[i].getBytes())
//                );
//            }

            javaMailSender.send(mimeMessage);
            f=true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return f;
    }

}
