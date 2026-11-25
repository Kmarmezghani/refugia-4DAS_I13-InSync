package com.awd.veterinaire.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OuvertureDossierRequest(@NotNull @Positive Long animalId) {
}
