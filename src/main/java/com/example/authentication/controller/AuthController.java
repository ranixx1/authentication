package com.example.authentication.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.dto.CompleteResetRequest;
import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.ResetRequest;
import com.example.authentication.enums.Type;
import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.AuthService;
import com.example.authentication.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetService passwordResetService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User already exists"));

        }

        User user = new User();
        user.setName(request.getName());
        user.setBirthDate(request.getBirthDate());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(Type.ROLE_NORMAL);
        user.setActive(true);
        user.setFailedAttempts(0);

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String agent = httpRequest.getHeader("user-agent");

        String token = authService.login(request, ip, agent);

        // Retorna o objeto estruturado
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ResetRequest request) {
        String message = passwordResetService.passwordReset(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody CompleteResetRequest request) {
        passwordResetService.completePasswordReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}