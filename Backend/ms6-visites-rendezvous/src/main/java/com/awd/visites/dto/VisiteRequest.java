package com.awd.visites.dto;

import jakarta.validation.constraints.NotNull;

public record VisiteRequest(
        @NotNull Long visiteurId,
        @NotNull Long animalId,
        @NotNull Long creneauId,
        String nomAnimal,
        String espece
) {}