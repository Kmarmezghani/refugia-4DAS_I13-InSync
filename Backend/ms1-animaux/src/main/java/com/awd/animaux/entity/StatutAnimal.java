package com.awd.animaux.entity;

import java.util.EnumSet;
import java.util.Set;

/**
 * Statut d'un animal et transitions autorisées (dossier technique, section 7.1).
 */
public enum StatutAnimal {
    DISPONIBLE,
    RESERVE,
    EN_ACCUEIL,
    EN_SOINS,
    ADOPTE;

    /** Statuts vers lesquels on peut passer depuis ce statut. */
    public Set<StatutAnimal> suivantsAutorises() {
        return switch (this) {
            case DISPONIBLE -> EnumSet.of(RESERVE, EN_ACCUEIL, EN_SOINS, ADOPTE);
            case RESERVE -> EnumSet.of(DISPONIBLE, ADOPTE);
            case EN_ACCUEIL -> EnumSet.of(DISPONIBLE, RESERVE, EN_SOINS, ADOPTE);
            case EN_SOINS -> EnumSet.of(DISPONIBLE, EN_ACCUEIL);
            case ADOPTE -> EnumSet.noneOf(StatutAnimal.class); // état final
        };
    }

    public boolean peutPasserA(StatutAnimal cible) {
        return cible != null && suivantsAutorises().contains(cible);
    }
}
