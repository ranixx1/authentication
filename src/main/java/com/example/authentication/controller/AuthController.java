package com.example.authentication.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.ResetRequest;
import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.AuthService;
import com.example.authentication.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("Usuário criado");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String agent = httpRequest.getHeader("user-agent");

        String token = authService.login(request, ip, agent);

        // Retorna o objeto estruturado
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetRequest request) {
        String message = passwordResetService.passwordReset(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        passwordResetService.completePasswordReset(token, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }
}