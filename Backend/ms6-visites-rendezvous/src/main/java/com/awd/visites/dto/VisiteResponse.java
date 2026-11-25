package com.awd.visites.dto;

import com.awd.visites.entity.StatutVisite;

import java.time.LocalDateTime;

public record VisiteResponse(
        Long id,
        Long visiteurId,
        Long animalId,
        String nomAnimal,
        String espece,
        Long creneauId,
        StatutVisite statut,
        String motifAnnulation,
        LocalDateTime dateCreation
) {}