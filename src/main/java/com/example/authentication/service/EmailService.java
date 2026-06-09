package com.example.authentication.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendResetLink(
            String email,
            String token) {
        String link = "http://localhost:8080/auth/reset-password?token="
                + token;

        private static final Logger log = LoggerFactory.getLogger(EmailService.class);
        log.info("Sending reset link to: {}", email);
        log.debug("Reset link: {}", link);
    }

}
