package com.example.authentication.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.enums.FailureReason;
import com.example.exception.UnauthorizedException;
import com.example.authentication.model.LoginAudit;
import com.example.authentication.model.User;
import com.example.authentication.repository.LoginAuditRepository;
import com.example.authentication.repository.UserRepository;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, LoginAuditRepository loginAuditRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request, String ipAddress, String userAgent) {

        User user = userRepository.findByEmailOrUsername(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        // Check if account is disabled before any other validation
        if (!user.isActive()) {
            saveAudit(user, false, ipAddress, FailureReason.ACCOUNT_DISABLED, userAgent);
            throw new UnauthorizedException("Invalid credentials");
        }

        // Check if account is temporarily locked due to failed attempts
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            saveAudit(user, false, ipAddress, FailureReason.ACCOUNT_LOCKED, userAgent);
            throw new UnauthorizedException("Invalid credentials");
        }

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 5) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(5));
            }

            userRepository.save(user);
            saveAudit(user, false, ipAddress, FailureReason.BAD_CREDENTIALS, userAgent);
            throw new UnauthorizedException("Invalid credentials");
        }

        // Successful login — reset lock state and record last login
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        saveAudit(user, true, ipAddress, null, userAgent);

        return jwtService.generateToken(user);
    }

    private void saveAudit(User user, boolean success, String ipAddress,
            FailureReason reason, String userAgent) {
        LoginAudit audit = new LoginAudit();
        audit.setUser(user);
        audit.setLoginTime(LocalDateTime.now());
        audit.setIpAddress(ipAddress);
        audit.setUserAgent(userAgent);
        audit.setSuccess(success);
        audit.setReason(reason);
        loginAuditRepository.save(audit);
    }
}