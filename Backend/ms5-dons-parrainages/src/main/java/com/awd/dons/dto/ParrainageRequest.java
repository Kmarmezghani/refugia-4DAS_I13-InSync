package com.awd.dons.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ParrainageRequest(
        @NotNull(message = "Le donateur est obligatoire")
        Long donateurId,

        @NotNull(message = "L'animal est obligatoire")
        Long animalId,

        @NotNull(message = "Le montant mensuel est obligatoire")
        @Positive(message = "Le montant mensuel doit être positif")
        BigDecimal montantMensuel
) {
}
