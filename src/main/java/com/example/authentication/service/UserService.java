package com.example.authentication.service;

import org.springframework.stereotype.Service;

import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Definir limites de tentativas, padrão de sistema, é 5.
    // Criar reset de senhas, seja com um Recuperar senha, ou com o acerto
    // ((((ARMAZENAR ESSA LOG!))))
    // Caso não se recorde : active = false ---Temporariamente.

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean activateTwoFactorAuthentication(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuaário não encontrado"));


        if (user.isTwoFactorAuthentication() == true) {
            throw new IllegalArgumentException("Autenticação de dois fatores já está ativa.");
        }

        user.setTwoFactorAuthentication(true);
        userRepository.save(user);
        return true;
    }

}
