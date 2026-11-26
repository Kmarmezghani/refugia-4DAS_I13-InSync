package com.awd.adoptions.dto;

import com.awd.adoptions.entity.TypeLogement;
import jakarta.validation.constraints.*;

public record ProfilRequest(
        @NotBlank String clientId,   // temporaire : sera lu dans le jeton Keycloak en S8
        @NotNull TypeLogement typeLogement,
        boolean jardin,
        @Min(1) int nbPersonnes,
        boolean enfants,
        boolean autresAnimaux,
        String experienceAnimaux,
        String disponibilites) {
}