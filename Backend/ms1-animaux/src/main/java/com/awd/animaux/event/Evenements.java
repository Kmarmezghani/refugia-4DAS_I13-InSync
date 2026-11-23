package com.awd.animaux.event;

import com.awd.animaux.entity.Animal;
import com.awd.animaux.entity.HistoriqueStatut;

import java.util.LinkedHashMap;
import java.util.Map;

/** Les deux événements publiés par MS1 (contrat de la section 7.1). */
public final class Evenements {

    public static final String EXCHANGE = "refuge.events";
    public static final String ANIMAL_CREE = "AnimalCréé";
    public static final String STATUT_MODIFIE = "StatutAnimalModifié";
    public static final String RK_ANIMAL_CREE = "animal.cree";
    public static final String RK_STATUT_MODIFIE = "animal.statut.modifie";

    private Evenements() {
    }

    public static EvenementMetier animalCree(Animal a) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("animalId", a.getId());
        data.put("nom", a.getNom());
        data.put("espece", a.getEspece().name());
        data.put("statut", a.getStatut().name());
        return new EvenementMetier(RK_ANIMAL_CREE, EventEnvelope.of(ANIMAL_CREE, data));
    }

    public static EvenementMetier statutModifie(Animal a, HistoriqueStatut h) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("animalId", a.getId());
        data.put("ancienStatut", h.getAncienStatut().name());
        data.put("nouveauStatut", h.getNouveauStatut().name());
        data.put("origine", h.getOrigine());
        return new EvenementMetier(RK_STATUT_MODIFIE, EventEnvelope.of(STATUT_MODIFIE, data));
    }
}
