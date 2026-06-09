package com.example.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendResetLink(
            String email,
            String token) {
        String link = "http://localhost:8080/auth/reset-password?token="
                + token;

        log.info("Sending reset link to: {}", email);
        log.debug("Reset link: {}", link);
    }

}
