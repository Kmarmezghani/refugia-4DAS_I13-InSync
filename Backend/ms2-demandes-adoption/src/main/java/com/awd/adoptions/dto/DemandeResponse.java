package com.awd.adoptions.dto;

import com.awd.adoptions.entity.StatutDemande;
import java.time.LocalDateTime;

public record DemandeResponse(
        Long id, Long animalId, String clientId, StatutDemande statut,
        String motifRefus, LocalDateTime dateCreation, LocalDateTime dateDecision,
        Integer scoreTotal) {
}