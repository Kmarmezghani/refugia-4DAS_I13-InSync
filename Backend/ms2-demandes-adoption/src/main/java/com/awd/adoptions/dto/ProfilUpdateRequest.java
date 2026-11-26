package com.awd.adoptions.dto;

import com.awd.adoptions.entity.TypeLogement;
import jakarta.validation.constraints.*;

public record ProfilUpdateRequest(
        @NotNull TypeLogement typeLogement,
        boolean jardin,
        @Min(1) int nbPersonnes,
        boolean enfants,
        boolean autresAnimaux,
        String experienceAnimaux,
        String disponibilites) {
}