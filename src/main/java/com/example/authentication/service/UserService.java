package com.example.authentication.service;

import org.springframework.stereotype.Service;

import com.example.authentication.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


        
}
