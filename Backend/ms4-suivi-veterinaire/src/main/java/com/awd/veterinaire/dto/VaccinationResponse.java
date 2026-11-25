package com.awd.veterinaire.dto;

import com.awd.veterinaire.entity.StatutVaccination;

import java.time.LocalDate;

public record VaccinationResponse(
        Long id,
        Long dossierId,
        String vaccin,
        LocalDate dateVaccination,
        LocalDate dateRappel,
        boolean rappelPublie,
        StatutVaccination statut
) {
}
