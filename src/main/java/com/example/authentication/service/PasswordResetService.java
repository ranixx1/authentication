package com.example.authentication.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.authentication.dto.ResetRequest;
import com.example.authentication.model.PasswordReset;
import com.example.authentication.model.User;
import com.example.authentication.repository.PasswordResetRepository;
import com.example.authentication.repository.UserRepository;

@Service
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PasswordResetService(
            PasswordResetRepository passwordResetRepository,
            UserRepository userRepository,
            EmailService emailService
        
        ) {

        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public String passwordReset(ResetRequest request) {

        User user = userRepository
                .findByEmailOrUsername(
                        request.getReset(),
                        request.getReset()
                )
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if (!user.getBirthDate().equals(request.getBirthDate())) {
            throw new RuntimeException("Invalid credentials");
        }

        PasswordReset token = new PasswordReset();

        token.setUser(user);

        token.setToken(UUID.randomUUID().toString());

        token.setExpireAt(
                LocalDateTime.now().plusMinutes(30)
        );

        token.setUsed(false);

        passwordResetRepository.save(token);

        emailService.sendResetLink(user.getEmail(), token.getToken());

        return "Recovery email sent";
    }
}

/*
 * ✔ criação de token
 * ✔ validação
 * ✔ expiração
 * ✔ redefinição de senha
 * ✔ invalidação do token
 * ✔ Armazenar a LOG da alteração
 */