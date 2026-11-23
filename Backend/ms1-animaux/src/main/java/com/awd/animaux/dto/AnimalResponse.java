package com.awd.animaux.dto;

import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.Sexe;
import com.awd.animaux.entity.StatutAnimal;
import com.awd.animaux.entity.Taille;

import java.time.LocalDate;
import java.util.List;

/** Fiche complète (GET /api/animaux/{id}), lue aussi par MS2 à MS6. */
public record AnimalResponse(
        Long id,
        String nom,
        Espece espece,
        String race,
        Sexe sexe,
        LocalDate dateNaissance,
        Taille taille,
        String description,
        int niveauEnergie,
        boolean ententeEnfants,
        boolean ententeAutresAnimaux,
        StatutAnimal statut,
        LocalDate dateArrivee,
        List<PhotoResponse> photos
) {
}
