package com.awd.dons.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecuResponse(
        Long id,
        String numero,
        LocalDateTime dateEmission,
        BigDecimal montant,
        Long paiementId,
        String donateurNom,
        String donateurEmail
) {
}
