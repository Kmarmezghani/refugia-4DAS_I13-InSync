package com.awd.dons.dto;

import com.awd.dons.entity.ModePaiement;
import com.awd.dons.entity.StatutPaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementResponse(
        Long id,
        Long donId,
        Long parrainageId,
        BigDecimal montant,
        LocalDateTime datePaiement,
        ModePaiement modePaiement,
        StatutPaiement statut,
        String numeroRecu
) {
}
