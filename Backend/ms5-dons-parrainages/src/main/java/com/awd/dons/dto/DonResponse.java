package com.awd.dons.dto;

import com.awd.dons.entity.StatutDon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonResponse(
        Long id,
        Long donateurId,
        String donateurNom,
        Long animalId,
        BigDecimal montant,
        StatutDon statut,
        LocalDateTime dateDon,
        String message,
        Long paiementId,
        String numeroRecu
) {
}
