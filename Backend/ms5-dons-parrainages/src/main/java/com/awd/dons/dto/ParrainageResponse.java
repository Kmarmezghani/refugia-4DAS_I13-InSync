package com.awd.dons.dto;

import com.awd.dons.entity.StatutParrainage;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParrainageResponse(
        Long id,
        Long donateurId,
        String donateurNom,
        Long animalId,
        BigDecimal montantMensuel,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutParrainage statut
) {
}
