package com.example.authentication.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.authentication.enums.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long userId;

    @Column
    private String name;


    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String phoneNumber;

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
    private boolean twoFactorAuthentication = false;


    @OneToMany(mappedBy = "user")
    private List<LoginAudit> loginAudits;

    public User(String name, String username, String phoneNumber, String email, Type type) {
        this.name = name;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.type = type;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}


// i thought to add a variable : isVerify, when the costumer sent the ID 