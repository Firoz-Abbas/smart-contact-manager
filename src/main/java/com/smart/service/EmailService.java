package com.smart.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmailService {
    boolean sendMail(String mailto, String subject, String body);
}
