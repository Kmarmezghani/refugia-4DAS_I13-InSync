package com.awd.animaux.dto;

import com.awd.animaux.entity.StatutAnimal;

import java.time.LocalDateTime;

public record HistoriqueResponse(
        Long id,
        StatutAnimal ancienStatut,
        StatutAnimal nouveauStatut,
        LocalDateTime dateChangement,
        String origine,
        String motif
) {
}
