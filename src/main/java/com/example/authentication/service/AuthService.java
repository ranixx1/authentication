package com.example.authentication.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.enums.FailureReason;
import com.example.authentication.model.LoginAudit;
import com.example.authentication.model.User;
import com.example.authentication.repository.LoginAuditRepository;
import com.example.authentication.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, LoginAuditRepository loginAuditRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest request, String ip, String agent) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            saveAudit(user, false, ip, FailureReason.ACCOUNT_LOCKED, agent);

            throw new RuntimeException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 5) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(5));
            }

            userRepository.save(user);

            saveAudit(user, false, ip, FailureReason.BAD_CREDENTIALS, agent);

            throw new RuntimeException("Invalid credentials");
        }

        // login sucess?

        user.setFailedAttempts(0);
        user.setLockUntil(null);
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        saveAudit(user, true, ip, null, agent);

        return "TOKEN_AQUI";
    }

    private void saveAudit(

            User user,
            boolean sucess,
            String ip,
            FailureReason reason,
            String agent) {

        LoginAudit audit = new LoginAudit();
        audit.setUser(user);
        audit.setLoginTime(LocalDateTime.now());
        audit.setIpAddress(ip);
        audit.setUserAgent(agent);
        audit.setSuccess(sucess);
        audit.setReason(reason);

        loginAuditRepository.save(audit);
    }
}
