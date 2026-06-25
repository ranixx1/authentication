package com.example.authentication.service;

import org.springframework.stereotype.Service;

import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);
    }

    public boolean activateTwoFactorAuthentication(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isTwoFactorAuthentication()) {
            throw new IllegalArgumentException("Two-factor authentication is already enabled.");
        }

        user.setTwoFactorAuthentication(true);
        userRepository.save(user);
        return true;
    }

}
