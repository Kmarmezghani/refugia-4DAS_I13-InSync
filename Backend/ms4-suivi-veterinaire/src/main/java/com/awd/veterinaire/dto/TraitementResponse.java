package com.awd.veterinaire.dto;

import com.awd.veterinaire.entity.StatutTraitement;

import java.time.LocalDate;

public record TraitementResponse(
        Long id,
        Long dossierId,
        String medicament,
        String posologie,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutTraitement statut
) {
}
