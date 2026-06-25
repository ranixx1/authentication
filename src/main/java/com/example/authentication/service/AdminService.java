package com.example.authentication.service;

import com.example.authentication.dto.admin.AuditSummary;
import com.example.authentication.dto.admin.UserDetailResponse;
import com.example.authentication.dto.admin.UserSummaryResponse;
import com.example.authentication.enums.Type;
import com.example.authentication.model.LoginAudit;
import com.example.authentication.model.User;
import com.example.authentication.repository.LoginAuditRepository;
import com.example.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;

    public List<UserSummaryResponse> listarUsuarios() {
        return userRepository.findAll().stream()
                .map(u -> new UserSummaryResponse(
                        u.getUserId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getType() != null ? u.getType().name() : null,
                        u.isActive(),
                        u.getLastLogin(),
                        u.getCreatedAt()
                ))
                .toList();
    }

    public UserDetailResponse detalharUsuario(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<LoginAudit> audits = loginAuditRepository
                .findTop10ByUserOrderByLoginTimeDesc(user);

        List<AuditSummary> auditSummaries = audits.stream()
                .map(a -> new AuditSummary(
                        a.getLoginTime(),
                        a.getIpAddress(),
                        a.isSuccess(),
                        a.getReason() != null ? a.getReason().name() : null,
                        a.getUserAgent()
                ))
                .toList();

        return new UserDetailResponse(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getType() != null ? user.getType().name() : null,
                user.isActive(),
                user.getFailedAttempts(),
                user.getCreatedAt(),
                user.getLastLogin(),
                auditSummaries
        );
    }

    public void alterarRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setType(Type.valueOf(role));
        userRepository.save(user);
    }

    public void toggleAtivo(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setActive(active);
        userRepository.save(user);
    }
}