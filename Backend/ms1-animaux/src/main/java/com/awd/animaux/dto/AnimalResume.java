package com.awd.animaux.dto;

import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.Sexe;
import com.awd.animaux.entity.StatutAnimal;
import com.awd.animaux.entity.Taille;

/** Ligne du catalogue (GET /api/animaux). */
public record AnimalResume(
        Long id,
        String nom,
        Espece espece,
        String race,
        Sexe sexe,
        Taille taille,
        StatutAnimal statut,
        String photoPrincipale
) {
}
