package com.example.authentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authentication.model.LoginAudit;

public interface LoginAuditRepository extends JpaRepository <LoginAudit, Long> {
    
    List<LoginAudit> findByCity(String city);
    List<LoginAudit> findByCountry(String country);


}

/*
  @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime loginTime;

    private String ipAddress; 
    private String country;
    private String city;

    private String userAgent; // navegador/dispositivo  // Não vejo como interessante filtrar por navegador ou dispositivo.

    private boolean success; // true = login OK, false = erro // Seria interessante retornar uma lista com todos os acessos com sucesso?
    List<LoginAudit> findBySucess(String sucess) **

    private String failureReason; // (ex: BAD_CREDENTIALS)  // Seria interessante retornar uma lista com registros de LOG com o erro especificado?
    List<LoginAudit> findyByFailureReason(String failureReason) **
}
*/