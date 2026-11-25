package com.awd.veterinaire.mapper;

import com.awd.veterinaire.dto.TraitementResponse;
import com.awd.veterinaire.entity.Traitement;
import org.springframework.stereotype.Component;

@Component
public class TraitementMapper {

    public TraitementResponse versResponse(Traitement traitement) {
        return new TraitementResponse(
                traitement.getId(),
                traitement.getDossier().getId(),
                traitement.getMedicament(),
                traitement.getPosologie(),
                traitement.getDateDebut(),
                traitement.getDateFin(),
                traitement.getStatut()
        );
    }
}
