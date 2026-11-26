package com.awd.dons.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DonRequest(
        @NotNull(message = "Le donateur est obligatoire")
        Long donateurId,

        @NotNull(message = "L'animal est obligatoire")
        Long animalId,

        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit être positif")
        BigDecimal montant,

        String message
) {
}