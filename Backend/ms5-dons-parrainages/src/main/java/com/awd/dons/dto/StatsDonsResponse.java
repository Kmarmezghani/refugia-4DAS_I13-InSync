package com.awd.dons.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record StatsDonsResponse(
        LocalDate debut,
        LocalDate fin,
        BigDecimal totalCollecte,
        long nombreDons,
        long nombrePaiementsParrainage,
        List<AnimalSoutenu> animauxLesPlusSoutenus
) {
    public record AnimalSoutenu(Long animalId, BigDecimal montantTotal) {
    }
}
