package com.awd.visites.dto;

import com.awd.visites.entity.TypeEvenement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EvenementRequest(
        @NotBlank String titre,
        @NotNull TypeEvenement type,
        String description,
        @NotNull LocalDateTime date,
        @NotBlank String lieu,
        @Min(1) int capacite
) {}