package com.awd.animaux.dto;

import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.StatutAnimal;

import java.util.Map;

/**
 * GET /api/animaux/statistiques.
 * dureeMoyenneSejourJours : moyenne (en jours) entre l'arrivée et l'adoption, null si aucun animal adopté.
 */
public record StatistiquesResponse(
        long total,
        Map<StatutAnimal, Long> parStatut,
        Map<Espece, Long> parEspece,
        Double dureeMoyenneSejourJours
) {
}
