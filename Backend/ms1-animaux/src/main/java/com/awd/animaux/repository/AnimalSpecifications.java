package com.awd.animaux.repository;

import com.awd.animaux.entity.Animal;
import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.StatutAnimal;
import com.awd.animaux.entity.Taille;
import org.springframework.data.jpa.domain.Specification;

/** Filtres de GET /api/animaux : chaque filtre absent est ignoré. */
public final class AnimalSpecifications {

    private AnimalSpecifications() {
    }

    public static Specification<Animal> filtrer(Espece espece, StatutAnimal statut, Taille taille, String q) {
        Specification<Animal> spec = (root, query, cb) -> cb.conjunction();

        if (espece != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("espece"), espece));
        }
        if (statut != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("statut"), statut));
        } else {
            // Sans filtre de statut, le catalogue n'affiche pas les animaux adoptés.
            spec = spec.and((root, query, cb) -> cb.notEqual(root.get("statut"), StatutAnimal.ADOPTE));
        }
        if (taille != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("taille"), taille));
        }
        if (q != null && !q.isBlank()) {
            String motif = "%" + q.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("nom")), motif),
                    cb.like(cb.lower(root.get("race")), motif),
                    cb.like(cb.lower(root.get("description")), motif)));
        }
        return spec;
    }
}
