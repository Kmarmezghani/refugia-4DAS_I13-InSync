package com.awd.animaux.dto;

import com.awd.animaux.entity.StatutAnimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Corps de PATCH /api/animaux/{id}/statut.
 * origine : qui demande le changement (PERSONNEL par défaut, MS2 pour une adoption, MS3 pour un accueil).
 */
public record ChangementStatutRequest(
        @NotNull StatutAnimal nouveauStatut,
        @Size(max = 255) String motif,
        @Size(max = 30) String origine
) {
}
