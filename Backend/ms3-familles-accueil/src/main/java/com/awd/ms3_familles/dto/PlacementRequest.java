package com.awd.ms3_familles.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PlacementRequest(
        @NotNull(message = "L'ID de l'animal est obligatoire")
        Long animalId,

        @NotNull(message = "L'ID de la famille est obligatoire")
        Long familleId,

        @NotNull(message = "La date de début est obligatoire")
        @FutureOrPresent(message = "La date de début ne peut pas être dans le passé")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin prévue est obligatoire")
        LocalDate dateFinPrevue,

        String notes
) {}