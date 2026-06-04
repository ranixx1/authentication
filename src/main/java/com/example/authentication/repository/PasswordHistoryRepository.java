package com.example.authentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authentication.enums.PasswordChangeReason;
import com.example.authentication.model.PasswordHistory;
import com.example.authentication.model.User;

public interface PasswordHistoryRepository extends JpaRepository <PasswordHistory, Long>{
    List<PasswordHistory> findByUser(User user);
    List<PasswordHistory> findByReason(PasswordChangeReason reason);
    List<PasswordHistory> findByUserOrderByChangedAtDesc(User user);
    
}
