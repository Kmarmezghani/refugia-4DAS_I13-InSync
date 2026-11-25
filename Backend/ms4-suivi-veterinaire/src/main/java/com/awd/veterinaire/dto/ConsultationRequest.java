package com.awd.veterinaire.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ConsultationRequest(
        @NotNull @Positive @Schema(example = "1") Long dossierId,
        @NotNull LocalDate dateConsultation,
        @NotBlank @Schema(example = "Contrôle de routine") String motif,
        String diagnostic,
        @Positive Double poids,
        Double temperature,
        String remarques
) {
}
