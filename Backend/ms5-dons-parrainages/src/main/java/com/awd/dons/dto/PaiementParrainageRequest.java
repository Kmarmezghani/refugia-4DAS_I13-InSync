package com.awd.dons.dto;

import com.awd.dons.entity.ModePaiement;
import jakarta.validation.constraints.NotNull;

public record PaiementParrainageRequest(
        @NotNull(message = "Le mode de paiement est obligatoire")
        ModePaiement modePaiement
) {
}
