package com.example.authentication.service;

import org.springframework.stereotype.Service;

import com.example.authentication.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;


    // Definir limites de tentativas, padrão de sistema, é 5.
    // Criar reset de senhas, seja com um Recuperar senha, ou com o acerto ((((ARMAZENAR ESSA LOG!))))
    // Caso não se recorde : active = false ---Temporariamente.



    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public 
        
}
