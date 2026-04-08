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

    @Column (nullable = false)
    private String numberPhone;

    @Column (nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column
    @Enumerated(EnumType.STRING)    // Criar formulário que permita um USER solicitar um novo ROLE
    private Type type;

    @Column(nullable = false)
    private String password;

    private Boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLogin;

    private Integer failedAttempts = 0; // utilizar service para limitar.

    public User(String username, String numberPhone, String email, String cpf, Type type) {
        this.username = username;
        this.numberPhone = numberPhone;
        this.email = email;
        this.cpf = cpf;
        this.type = type;
    }
}
