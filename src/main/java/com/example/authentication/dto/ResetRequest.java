package com.example.authentication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResetRequest {
    private String reset;
    private LocalDate birthDate;
}
