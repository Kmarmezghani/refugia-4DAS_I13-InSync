package com.awd.ms3_familles.mapper;

import com.awd.ms3_familles.dto.PlacementRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.entity.Placement;
import org.springframework.stereotype.Component;

@Component
public class PlacementMapper {

    public Placement toEntity(PlacementRequest request, FamilleAccueil famille) {
        if (request == null) return null;

        Placement placement = new Placement();
        placement.setAnimalId(request.animalId());
        placement.setFamille(famille);
        placement.setDateDebut(request.dateDebut());
        placement.setDateFinPrevue(request.dateFinPrevue());
        placement.setNotes(request.notes());
        return placement;
    }
}