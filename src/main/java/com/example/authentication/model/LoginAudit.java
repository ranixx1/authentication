package com.example.authentication.model;

import java.time.LocalDateTime;

import com.example.authentication.enums.FailureReason;

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

    @Enumerated(EnumType.STRING)
    private FailureReason reason; // (ex: BAD_CREDENTIALS)


    /*@Override
    public String Tostring(){
        return "";                            // Seria interessante, para debug.

    }
    */
}

