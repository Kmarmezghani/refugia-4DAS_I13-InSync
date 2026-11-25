package com.awd.veterinaire.dto;

import java.time.LocalDate;

public record ConsultationResponse(
        Long id,
        Long dossierId,
        LocalDate dateConsultation,
        String motif,
        String diagnostic,
        Double poids,
        Double temperature,
        String remarques
) {
}
