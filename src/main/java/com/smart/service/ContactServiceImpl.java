package com.smart.service;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ContactServiceImpl implements ContactService  {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Contact addContactt(Contact contact) throws IOException {
//        MultipartFile[] file=contact.getImage();
//        String fname="";
//        for (int i=0; i<file.length; i++){
//            fname=fname +file[i].getOriginalFilename();
//        }
//        contact.setImage(fname);
//        userRepository.save(contact);
        return contact;
    }
}
