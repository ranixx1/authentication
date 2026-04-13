package com.example.authentication.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "login_audit")
@Getter
@Setter
public class LoginAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação com usuário
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime loginTime;

    private String ipAddress;
    private String country;
    private String city;

    private String userAgent; // navegador/dispositivo

    private boolean success; // true = login OK, false = erro

    private String failureReason; // (ex: BAD_CREDENTIALS)
}