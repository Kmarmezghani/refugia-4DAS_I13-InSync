package com.awd.adoptions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DemandeRequest(
        @NotNull Long animalId,
        @NotBlank String clientId) {   // temporaire (jeton Keycloak en S8)
}