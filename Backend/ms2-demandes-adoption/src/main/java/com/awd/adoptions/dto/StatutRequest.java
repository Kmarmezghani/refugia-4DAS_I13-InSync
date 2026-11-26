package com.awd.adoptions.dto;

import com.awd.adoptions.entity.StatutDemande;
import jakarta.validation.constraints.NotNull;

public record StatutRequest(
        @NotNull StatutDemande statut,
        String motifRefus) {
}