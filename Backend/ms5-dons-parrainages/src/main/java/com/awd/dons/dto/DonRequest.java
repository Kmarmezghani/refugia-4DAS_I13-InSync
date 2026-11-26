package com.awd.dons.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public record DonRequest(
        @NotNull(message = "Le donateur est obligatoire")
        Long donateurId,

        Long animalId,

        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit être positif")
        BigDecimal montant,

        @Size(max = 500)
        String message
) {
}