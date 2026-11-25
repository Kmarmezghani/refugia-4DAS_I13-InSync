package com.awd.visites.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreneauResponse(
        Long id,
        LocalDate date,
        LocalTime heureDebut,
        LocalTime heureFin,
        int capacite,
        int placesRestantes) {
}