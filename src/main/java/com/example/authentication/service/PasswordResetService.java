package com.example.authentication.service;

import org.springframework.stereotype.Service;

import com.example.authentication.dto.ResetRequest;
import com.example.authentication.model.User;
import com.example.authentication.repository.PasswordResetRepository;
import com.example.authentication.repository.UserRepository;

@Service
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;

    public PasswordResetService(PasswordResetRepository passwordResetRepository, UserRepository userRepository) {
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
    }

    public String passwordReset(ResetRequest request) {
        User user = userRepository.findByEmailOrUsername(request.getReset(), request.getReset())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getBirthDate().equals(request.getBirthDate())) {
            throw new RuntimeException("Invalid credentials");

        }
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