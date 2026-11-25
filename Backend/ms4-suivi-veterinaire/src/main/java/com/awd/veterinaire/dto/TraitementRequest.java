package com.awd.veterinaire.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TraitementRequest(
        @NotNull @Positive @Schema(example = "1") Long dossierId,
        @NotBlank String medicament,
        @NotBlank String posologie,
        @NotNull LocalDate dateDebut,
        LocalDate dateFin
) {
}
