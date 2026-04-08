package com.example.authentication.model;

import java.time.LocalDateTime;

import com.example.authentication.enums.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")

@Getter
@Setter
public class User {
    public User() {
        // Construtor vazio.
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String numberPhone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    @Enumerated(EnumType.STRING) // Criar formulário que permita um USER solicitar um novo ROLE
    private Type type;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLogin;

    private boolean active = true;

    private Integer failedAttempts = 0; // utilizar service para limitar.
    private LocalDateTime lockUntil; // bloqueia temporariamente


    // Separar // > Criar um novo arquivo para registrar em LOG o histórico do login.

    private String lastLoginIp; // salva IP de onde acessou pela última vez.
    private String lastLoginCountry; // salva país.
    private String lastLoginCity; // salva cidade.

    public User(String username, String numberPhone, String email, Type type) {
        this.username = username;
        this.numberPhone = numberPhone;
        this.email = email;
        this.type = type;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
