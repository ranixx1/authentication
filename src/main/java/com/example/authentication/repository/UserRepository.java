package com.example.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authentication.enums.Type;
import com.example.authentication.model.User;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername( String email,String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByType(Type type);
    List<User> findByActive(boolean active);

}
