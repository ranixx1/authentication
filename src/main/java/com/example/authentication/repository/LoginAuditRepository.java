package com.example.authentication.repository;

import com.example.authentication.enums.FailureReason;
import com.example.authentication.model.LoginAudit;
import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {

    List<LoginAudit> findByCity(String city);
    List<LoginAudit> findByCountry(String country);
    List<LoginAudit> findBySuccess(boolean success);
    List<LoginAudit> findByReason(FailureReason reason);
    List<LoginAudit> findTop10ByUserOrderByLoginTimeDesc(User user);
}