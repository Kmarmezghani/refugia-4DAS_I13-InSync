package com.awd.dons.dto;


import java.time.LocalDate;

public record DonateurResponse(
        Long id,
        String nom,
        String email,
        String telephone,
        LocalDate dateInscription
) {
}