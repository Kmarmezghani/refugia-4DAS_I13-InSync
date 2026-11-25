package com.awd.visites.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreneauRequest(
        @NotNull LocalDate date,
        @NotNull LocalTime heureDebut,
        @NotNull LocalTime heureFin,
        @Min(1) int capacite) {
}