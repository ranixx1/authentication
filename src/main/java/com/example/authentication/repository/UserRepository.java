package com.example.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authentication.enums.Type;
import com.example.authentication.model.User;

@Repository

public interface UserRepository extends JpaRepository<Integer, User> {
    
    Optional<User> findByEmail(String email);
    Optional<User> findByNumberPhone(String numberPhone);
    List<User> findByType(Type type);
}
