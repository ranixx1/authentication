package com.example.authentication.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authentication.dto.ResetRequest;
import com.example.authentication.enums.PasswordChangeReason;
import com.example.authentication.model.PasswordHistory;
import com.example.authentication.model.PasswordReset;
import com.example.authentication.model.User;
import com.example.authentication.repository.PasswordHistoryRepository;
import com.example.authentication.repository.PasswordResetRepository;
import com.example.authentication.repository.UserRepository;

@Service
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public PasswordResetService(
            PasswordResetRepository passwordResetRepository,
            UserRepository userRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            PasswordHistoryRepository passwordHistoryRepository
        
        ) {

        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.passwordHistoryRepository = passwordHistoryRepository;
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

    public void completePasswordReset(String tokenValue, String newPassword){
        PasswordReset resetToken = passwordResetRepository.findByToken(tokenValue).orElseThrow(()-> new RuntimeException("Invalid request"));

        if(resetToken.isUsed() || resetToken.getExpireAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Invalid credentials");
        }

        User user = resetToken.getUser();

        if(passwordEncoder.matches(newPassword, user.getPassword())){
            throw new RuntimeException("The new password cannot be the same the current password");
        }

        List<PasswordHistory> histories = passwordHistoryRepository.findByUser(user);
        for(PasswordHistory history:histories){
            if(history.getOldPassword() != null && passwordEncoder.matches(newPassword, history.getOldPassword())){
                throw new RuntimeException("The password has been used recently");
            }
        }

        String oldPasswordEncrypted = user.getPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetRepository.save(resetToken);

        PasswordHistory history = new PasswordHistory();
        history.setUser(user);
        history.setOldPassword(oldPasswordEncrypted);
        history.setReason(PasswordChangeReason.RESET_PASSWORD);
        passwordHistoryRepository.save(history);
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