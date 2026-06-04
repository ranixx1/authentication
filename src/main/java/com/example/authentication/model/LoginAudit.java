package com.example.authentication.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.authentication.enums.FailureReason;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "login_audit")
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Enumerated(EnumType.STRING)
    private FailureReason reason; // (ex: BAD_CREDENTIALS)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
}

/*
 * @Override
 * public String Tostring(){
 * return ""; // Seria interessante, para debug.
 * 
 * }
 */
