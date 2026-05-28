package com.example.authentication.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendResetLink(
        String email,
        String token
    ){
        String link = "http://localhost:8080/auth/reset-password?token="
                        + token;

        System.out.println(("enviando link para: " + email));

        System.out.println("Link: " + link);
    }
    
}
