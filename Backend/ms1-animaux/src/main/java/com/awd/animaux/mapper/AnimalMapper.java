package com.awd.animaux.mapper;

import com.awd.animaux.dto.*;
import com.awd.animaux.entity.Animal;
import com.awd.animaux.entity.HistoriqueStatut;
import com.awd.animaux.entity.Photo;

/** Conversion entité → DTO : l'API n'expose jamais directement les entités JPA. */
public final class AnimalMapper {

    private AnimalMapper() {
    }

    public static AnimalResponse toResponse(Animal a) {
        return new AnimalResponse(
                a.getId(), a.getNom(), a.getEspece(), a.getRace(), a.getSexe(), a.getDateNaissance(),
                a.getTaille(), a.getDescription(), a.getNiveauEnergie(), a.isEntenteEnfants(),
                a.isEntenteAutresAnimaux(), a.getStatut(), a.getDateArrivee(),
                a.getPhotos().stream().map(AnimalMapper::toResponse).toList());
    }

    public static AnimalResume toResume(Animal a) {
        String principale = a.getPhotos().stream()
                .filter(Photo::isPrincipale)
                .map(Photo::getUrl)
                .findFirst()
                .orElse(null);
        return new AnimalResume(a.getId(), a.getNom(), a.getEspece(), a.getRace(), a.getSexe(),
                a.getTaille(), a.getStatut(), principale);
    }

    public static PhotoResponse toResponse(Photo p) {
        return new PhotoResponse(p.getId(), p.getUrl(), p.isPrincipale(), p.getDateAjout());
    }

    public static HistoriqueResponse toResponse(HistoriqueStatut h) {
        return new HistoriqueResponse(h.getId(), h.getAncienStatut(), h.getNouveauStatut(),
                h.getDateChangement(), h.getOrigine(), h.getMotif());
    }
}
