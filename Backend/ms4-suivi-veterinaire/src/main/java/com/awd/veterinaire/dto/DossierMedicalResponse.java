package com.awd.veterinaire.dto;

import java.time.LocalDate;

public record DossierMedicalResponse(
        Long id,
        Long animalId,
        String numeroDossier,
        LocalDate dateOuverture,
        String notesGenerales
) {
}
